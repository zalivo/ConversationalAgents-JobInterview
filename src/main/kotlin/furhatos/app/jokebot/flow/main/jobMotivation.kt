package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.app.jokebot.nlu.motivatedUser
import furhatos.app.jokebot.nlu.socialMedia
import furhatos.app.jokebot.nlu.unMotivatedUser
import furhatos.flow.kotlin.*

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


val roleInterest: State = state(Parent) {

    //Starting the real questions in the interview
    onEntry {
        //User has 30 seconds to answer
        furhat.ask("Okay now, why does this role interest you?", timeout = 30000)
    }

    //User answers with motivational intent
    onResponse<motivatedUser> {
        furhat.say("That sounds really good!")
        //Go to next question
        goto(positionExpectations)
    }

    //User answers with unmotivational intent (look nlu)
    /**
     * We should think about more ways how we could answer
     */
    onResponse<unMotivatedUser> {
        furhat.say("Alrighty then, moving on.")
        //Go to next question
        goto(positionExpectations)
    }

    //User answers with none of the trigger words/phrases in nlu
    onResponse {
        furhat.say("Okay, that sounds good.")
        //Go to next question
        goto(positionExpectations)
    }

    //User doesn't answer, proceed to repetition (look nlu)
    onNoResponse {
        furhat.say("Sorry I could not hear you. Could you repeat that?")
        goto(repeatMotivation)
    }
}

//For repetition of motivation question
val repeatMotivation: State = state(Parent) {
    onEntry {
        //User has 30 seconds to answer
        furhat.listen(timeout = 30000)
    }

    //User responds with motivational intent
    onResponse<motivatedUser> {
        furhat.say("That sounds really good!")
        goto(positionExpectations)
    }

    //User answers with unmotivational intent
    onResponse<unMotivatedUser> {
        furhat.say("Alrighty then, moving on.")
        goto(positionExpectations)
    }

    //User answers with none of the trigger words/phrases in nlu
    onResponse {
        furhat.say("Okay, that sounds good.")
        //Go to next question
        goto(positionExpectations)
    }

    //User doesn't respond, proceed anyway
    onNoResponse {
        furhat.say("I couldn't hear you now either. Could you repeat that?")
        //We re-enter the repeating, robot will listen again
        //User can then repeat their answer
        reentry()
    }
}

/** TODO
 * in positionExpectations and growth we should either created seperated word list that we want to listen for and
 * probably comment or we could keep the motivated and unMotivatedUser
 */

val positionExpectations: State = state(Parent) {
    onEntry {
        furhat.ask(
            "I'd like to ask you what are your expectations from this position. Not salaray wise but" +
                    "rather about the experience you can get or projects you might work on."
        )
    }
    onResponse<motivatedUser> {
        goto(growth)
    }
    onResponse {
        goto(growth)
    }
}

val growth: State = state(Parent) {
    onEntry {
        furhat.ask("How do you think this position will improve your professional and personal skills?")
    }
    onResponse {
        goto(hearAboutPosition)
    }
}

//Question where answer doesn't need to be stored
val hearAboutPosition: State = state(Parent) {

    onEntry {
        //User has 30 seconds to answer
        furhat.ask("And where did you hear about this position?", timeout = 30000)
    }
    onResponse<socialMedia> {
        furhat.say(
            "We've been working on our public profile there for quite some time so we are glad you found" +
                    "us there."
        )
        goto(companyReason)
    }

    //ADD SOCIAL MEDIA ANSWER
    //User answers something random, we proceed to next question
    onResponse {
        furhat.say("Okay well that sounds nice! We are really happy that you are here.")
        goto(companyReason)
    }

    //User doesn't answer -> Robot asks for repetition
    onNoResponse {
        furhat.say("Could you repeat that?")
        goto(repeatHearAboutPosition)
    }

}

//For repetition of "hear about position" question
val repeatHearAboutPosition: State = state(Parent) {
    onEntry {
        //User has 30 seconds to answer
        furhat.listen(timeout = 30000)
    }

    //User responds, we go to next question
    onResponse {
        furhat.say("Okay well that sounds nice! We are really happy that you are here.")
        goto(companyReason)
    }

    //User doesn't answer, repeat
    onNoResponse {
        furhat.say("I can't hear you. Could you repeat yourself?")
        //Re-enter repetition
        reentry()
    }
}

val companyReason: State = state(Parent) {

    onEntry {
        furhat.ask("Could you please tell me why would you like to work specifically in our company?")
    }
    onResponse {
        furhat.say("Text")
        goto(skillSecIntro)
    }
}
/*
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
*/