package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state

val EndSecIntro : State = state(Parent) {
    onEntry{
        furhat.ask("We have reached the end of this interview. Do you have any questions or remarks regarding" +
                "the interview?")
    }
    onResponse{
        furhat.say("Text")
        goto(RealInterviewEnd)
    }
}

val RealInterviewEnd : State = state(Parent){
    onEntry{
        furhat.ask("Thank you for this interview. It has been a pleasure. Our HR team will evaluate this interview" +
                "and will let you know if you will be invited to an in person interview. Thank you and have a" +
                "great day.")
    }
}