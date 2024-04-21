package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val endSecIntro: State = state(Parent) {
    onEntry {

        furhat.ask(
            "We have reached the end of this interview. Do you have any questions or remarks regarding " +
                    "this interview?"
        )
    }

    onResponse<Yes> {
        goto(anyQuestions)
    }

    onResponse<No> {
        goto(realInterviewEnd)
    }

    onResponse {
        goto(anyQuestions)
    }

    onNoResponse {
        furhat.say("I couldn't hear you. I'll repeat myself.")
        reentry()
    }
}

val anyQuestions: State = state(Parent) {
    onEntry {
        furhat.ask("Okay! The questions you now might have regarding this interview " +
        "can be sent to our HR email at hr@ourcompany.nl. They will reach you later " +
        "regarding whatever you still might have in mind.")
    }

    onResponse {
        furhat.say("Alright!")
        goto(realInterviewEnd)
    }

    onNoResponse {
        goto(realInterviewEnd)
    }
}


val realInterviewEnd: State = state(Parent) {
    onEntry {
        furhat.ask(
            "Thank you for this interview, it has been a pleasure. Our HR team will evaluate this interview" +
                    "and they will let you know if you will be invited to an in person interview. Thank you and have a" +
                    "great day."
        )
    }
}