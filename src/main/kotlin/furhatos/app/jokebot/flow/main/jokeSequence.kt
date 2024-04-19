package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.app.jokebot.jokes.*
import furhatos.app.jokebot.nlu.BadJoke
import furhatos.app.jokebot.nlu.GoodJoke
import furhatos.app.jokebot.nlu.MotivatedUser
import furhatos.app.jokebot.nlu.UnMotivatedUser
import furhatos.app.jokebot.util.calculateJokeScore
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.skills.emotions.UserGestures

/**
 * This state gets jokes, tells jokes, and records the user's response to it.
 *
 * Once the joke has been told, prompts the user if they want to hear another joke.
 */

/** In interview bot we want to proceed with questions and respond according to
 * the user's answers.
 */

/** NOTE!! THE CODING HAS BEEN DONE FOR INTERVIEW BOT UNTIL "ROLE INTEREST"
 * QUESTION. KEEP THIS IN MIND WHILE RUNNING THE ROBOT.
 */


val RoleInterest: State = state(Parent) {

    //Starting the real questions in the interview
    onEntry {
        //User has 30 seconds to answer
        furhat.ask("Okay now, why does this role interest you?", timeout = 30000)
    }

    //User answers with motivational intent
    onResponse<MotivatedUser>{
        furhat.say("That sounds really good!")
        //Go to next question
        goto(HearAboutPosition)
    }

    //User answers with unmotivational intent (look nlu)
    onResponse<UnMotivatedUser>{
        furhat.say("Alrighty then, moving on.")
        //Go to next question
        goto(HearAboutPosition)
    }

    //User answers with none of the trigger words/phrases in nlu
    onResponse {
        furhat.say("Okay, that sounds good.")
        //Go to next question
        goto(HearAboutPosition)
    }

    //User doesn't answer, proceed to repetition (look nlu)
    onNoResponse {
        furhat.say("Sorry I could not hear you. Could you repeat that?")
        goto(RepeatMotivation)
    }
}

//For repetition of motivation question
val RepeatMotivation: State = state(Parent) {
    onEntry {
        //User has 30 seconds to answer
        furhat.listen(timeout = 30000)
    }

    //User responds with motivational intent
    onResponse<MotivatedUser> {
        furhat.say("That sounds really good!")
        goto(HearAboutPosition)
    }

    //User answers with unmotivational intent
    onResponse<UnMotivatedUser> {
        furhat.say("Alrighty then, moving on.")
        goto(HearAboutPosition)
    }

    //User answers with none of the trigger words/phrases in nlu
    onResponse {
        furhat.say("Okay, that sounds good.")
        //Go to next question
        goto(HearAboutPosition)
    }

    //User doesn't respond, proceed anyway
    onNoResponse {
        furhat.say("I couldn't hear you now either. Could you repeat that?")
        //We re-enter the repeating, robot will listen again
        //User can then repeat their answer
        reentry()
    }
}

//Question where answer doesn't need to be stored
val HearAboutPosition: State = state(Parent) {

    onEntry {
        //User has 30 seconds to answer
        furhat.ask("And where did you hear about this position?", timeout = 30000)
    }

    //User answers something random, we proceed to next question
    onResponse {
        furhat.say("Okay well that sounds nice! We are really happy that you are here.")
        //REMOVE JOKESEQUENCE, ADD NEXT QUESTION INSTEAD
        goto(JokeSequence)
    }

    //User doesn't answer -> Robot asks for repetition
    onNoResponse {
        furhat.say("Could you repeat that?")
        goto(RepeatHearAboutPosition)
    }

}

//For repetition of "hear about position" question
val RepeatHearAboutPosition: State = state(Parent) {
    onEntry {
        //User has 30 seconds to answer
        furhat.listen(timeout = 30000)
    }

    //User responds, we go to next question
    onResponse {
        furhat.say("Okay well that sounds nice! We are really happy that you are here.")
        //REMOVE JOKESEQUENCE, ADD NEXT QUESTION INSTEAD
        goto(JokeSequence)
    }

    //User doesn't answer, repeat
    onNoResponse {
        furhat.say("I can't hear you. Could you repeat yourself?")
        //Re-enter repetition
        reentry()
    }
}

val JokeSequence: State = state(Parent) {

    onEntry {
        //Get joke from the JokeHandler
        val joke = JokeHandler.getJoke()

        //Build an utterance with the joke.
        val utterance = utterance {
            +getJokeComment(joke.score) //Get comment on joke, using the score
            +delay(200) //Short delay
            +joke.intro //Deliver the intro of the joke
            +delay(1500) //Always let the intro sink in
            +joke.punchline //Deliver the punchline of the joke.
        }

        furhat.say(utterance)

        /**
         * Calls a state which we know returns a joke score.
         */
        JokeHandler.changeJokeScore(call(JokeScore) as Double)

        //Prompt the user if they want to hear another joke.
        furhat.ask(continuePrompt.random())
    }

    onResponse<Yes> {
        furhat.say("Sweet!")
        reentry()
    }

    onResponse<No> {
        furhat.say("Alright, thanks for letting me practice!")
        if (users.count > 1) {
            furhat.attend(users.other)
            furhat.ask("How about you? do you want some jokes?")
        } else {
            goto(Idle)
        }
    }

    onResponse {
        furhat.ask("Yes or no?")
    }

    onNoResponse {
        furhat.ask("I didn't hear you.")
    }
}

/**
 * This state records the users reaction to a joke, and terminates with calculated joke value.
 * Joke value is based on if the user smiled and/or the user said it was a good/bad joke.
 */
val JokeScore: State = state(Parent) {

    var saidBadJoke = false
    var saidGoodJoke = false
    var timeSmiledAtJoke = 0L
    var timestampStartedLaughing = 0L
    val jokeTimeout = 4000 // We wait for a reaction for 4 seconds

    onEntry {
        furhat.listen()
    }

    onResponse<GoodJoke>(instant = true) {
        saidGoodJoke = true
    }

    onResponse<BadJoke>(instant = true) {
        saidBadJoke = true
    }

    onResponse(instant = true) {
        //Do nothing
    }

    onNoResponse(instant = true) {
        //Do nothing
    }

    onUserGesture(UserGestures.Smile, cond = { it.isCurrentUser }, instant = true) {
        timestampStartedLaughing = System.currentTimeMillis() //Timestamp the moment the user started smiling.
        propagate()
    }

    onUserGestureEnd(UserGestures.Smile, cond = { it.isCurrentUser }, instant = true) {
        timeSmiledAtJoke += System.currentTimeMillis() - timestampStartedLaughing //Calculating the amount of millis spent laughing
        timestampStartedLaughing = 0L
        propagate()
    }

    onTime(delay = jokeTimeout) {
        if (timestampStartedLaughing != 0L) {
            timeSmiledAtJoke += System.currentTimeMillis() - timestampStartedLaughing
        }

        furhat.say(getResponseOnUser(timeSmiledAtJoke != 0L, saidBadJoke, saidGoodJoke))

        terminate(calculateJokeScore(timeSmiledAtJoke, jokeTimeout, saidBadJoke, saidGoodJoke))
    }
}
