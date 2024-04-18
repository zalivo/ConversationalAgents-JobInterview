package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*

val Start : State = state(Parent) {

    //Robot Johnny welcomes user to job interview and asks for user's name
    onEntry {
        furhat.ask("Hi there. Welcome to this job interview for the IT junior position at our company. My name is Johnny, and I am going to lead this interview. What is your name?", timeout = 5000)
    }

    //Says "Nice to meet you *user name*"
    onResponse<PersonName> {
        furhat.say("Nice to meet you" + it.intent)
        goto(CanWeStart)
    }

    //If the user says something else or the name is not in library
    onResponse {
        furhat.say("Nice to meet you!")
        goto(CanWeStart)
    }

    //If the user does not respond, proceed anyway
    onNoResponse {
        goto(CanWeStart)
    }
}

//Starting the interview state
val CanWeStart: State = state(Parent) {

    onEntry {
        furhat.ask("Are you comfortable and ready to start the interview?", timeout = 4000)
    }

    //If user responds yes, proceed to tell me about yourself question
    onResponse<Yes> {
        furhat.say("All right, sounds good.")
        goto(TellMeAboutYourself)
    }

    //If user says no, proceed anyway
    onResponse<No> {
        furhat.say("Okay well that's a shame, we really need to start though.")
        goto(TellMeAboutYourself)
    }

    //If user responds something else, proceed anyway
    onResponse {
        furhat.say("I will take that as a yes.")
        goto(TellMeAboutYourself)
    }

    //If user doesn't respond, proceed anyway
    onNoResponse {
        furhat.say("I will take that as a yes.")
        goto(TellMeAboutYourself)
    }
}

//Tell me about yourself question, user gets to answer for 1 minute
val TellMeAboutYourself: State = state(Parent) {
    onEntry {
        furhat.ask("All right, so tell me a little bit about yourself.", timeout = 60000)
    }

    //User responds something, proceed
    onResponse {
        furhat.say("That sounds really interesting!")
        goto(RoleInterest)
    }

    //User doesn't respond, the user should repeat oneself
    onNoResponse{
        furhat.say("I can't really hear you. Could you repeat that?")
        goto(RepeatAboutYourself)
    }
}

//For repetition of tell me about yourself question
val RepeatAboutYourself: State = state(Parent) {
    onEntry {
        furhat.listen()
    }

    //User answers something, proceed
    onResponse{
        furhat.say("That sounds really interesting!")
        goto(RoleInterest)
    }

    //User doesn't respond, proceed anyway
    onNoResponse{
        goto(RoleInterest)
    }
    }

