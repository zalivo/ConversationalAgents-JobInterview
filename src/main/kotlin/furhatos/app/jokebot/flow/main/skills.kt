package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val skillSecIntro: State = state(Parent) {

    /**
     * we could probably use askYN for Yes/No question
     * TODO THE TIMER DOESN'T WORK
     */
    onEntry {
        furhat.ask("Now I would like to talk about your skills and experience. Can we proceed?")
    }

    onResponse<Yes> {
        furhat.say("Text")
        goto(pythonCheck)
    }

    onResponse<No> {
        furhat.say("In that case, we can take a short 3-minute break. Hopefully, we could continue right after.")

        // Set a timer for 10 seconds (10000 milliseconds)
        onTime(delay = 180000) {
            furhat.say("Ten seconds have passed.")
            reentry()
        }
    }

    onReentry {
        furhat.say("I hope the little break has helped. Can we continue now?")
    }
}

val pythonCheck: State = state(Parent) {
    onEntry {
        furhat.ask(
            "Because for this position some previous experience with Python would be appreciated I would" +
                    "like to ask if you have such experiences"
        )
    }
    onResponse {
        furhat.say("Text")
        goto(programmingLanguages)
    }
}

/**
 * We could listen if the person has experience with Python or not. Based on that we could add some sentence
 * before the default furhat.ask()
 * Example: In case of no experience: "That is unfortunate"
 */
val programmingLanguages: State = state(Parent) {
    onEntry {
        furhat.ask("Do you have any experience with some other programming languages? If so, please state them.")
    }
    onResponse {
        furhat.say("Text")
        goto(studies)
    }
}

val studies: State = state(Parent) {
    onEntry {
        furhat.ask(
            "In your CV you mentioned you study at University of Twente. Could you tell me more about" +
                    "your studies? Favorite subjects or projects that you have worked on."
        )
    }
    onResponse<Yes> {
        furhat.say("Text")
        goto(personalProjects)
    }
    onResponse<No> {
        furhat.say("Text")
    }
}

/**
 * We could have developing questions in case the user has built some personal projects
 * Like what did you struggle with, how was? what did you have to learn to finish the project?
 */
val personalProjects: State = state(Parent) {
    onEntry {
        furhat.ask(
            "Do you have any personal projects that you have worked on? Could be some personal programming" +
                    "project or something else, doesn't have to be commercial."
        )
    }
    onResponse<Yes> {
        furhat.say("Text")
        goto(internationalSetting)
    }
    onResponse<No> {
        furhat.say("That's unfortunate.")
        goto(internationalSetting)
    }
}

val internationalSetting: State = state(Parent) {
    onEntry {
        furhat.ask(
            "Have you ever worked in an international setting? Could be in school or in previous" +
                    "jobs. "
        )
    }
    onResponse<Yes> {
        furhat.say("Text")
        goto(teamRole)
    }
    onResponse<No> {
        furhat.say("Text")
        goto(teamRole)
    }
}

val teamRole: State = state(Parent) {
    onEntry {
        furhat.ask(
            "How would you describe your role in a team project? What kind of responsibilities do you" +
                    "usually take on?"
        )
    }
    onResponse {
        furhat.say("Text")
        goto(endSecIntro)
    }
}