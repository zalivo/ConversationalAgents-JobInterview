package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state

val SkillSecIntro : State = state(Parent) {

    onEntry {
        furhat.ask("Now I would like to talk about your skills and experience. Can we proceed?")
    }
    onResponse{
        furhat.say("Text")
        goto(PythonCheck)
    }
}

val PythonCheck : State = state(Parent) {
    onEntry{
        furhat.ask("Because for this position some previous experience with Python would be appreciated I would" +
                "like to ask if you have such experiences")
    }
    onResponse{
        furhat.say("Text")
        goto(ProgrammingLanguages)
    }
}

/**
 * We could listen if the person has experience with Python or not. Based on that we could add some sentence
 * before the default furhat.ask()
 * Example: In case of no experience: "That is unfortunate"
 */
val ProgrammingLanguages : State = state(Parent){
    onEntry{
        furhat.ask("Do you have any experience with some other programming languages? If so, please state them.")
    }
    onResponse{
        furhat.say("Text")
        goto(Studies)
    }
}

val Studies : State = state(Parent) {
    onEntry{
        furhat.ask("In your CV you mentioned you study at University of Twente. Could you tell me more about" +
                "your studies? Favorite subjects or projects that you have worked on.")
    }
    onResponse{
        furhat.say("Text")
        goto(PersonalProjects)
    }
}

/**
 * We could have developing questions in case the user has built some personal projects
 * Like what did you struggle with, how was? what did you have to learn to finish the project?
 */
val PersonalProjects : State = state(Parent){
    onEntry{
        furhat.ask("Do you have any personal projects that you have worked on? Could be some personal programming" +
                "project or something else, doesn't have to be commercial.")
    }
    onResponse{
        furhat.say("Text")
        goto(InternationalSetting)
    }
}

val InternationalSetting : State = state(Parent){
    onEntry{
        furhat.ask("Have you ever worked in an international setting? Could be in school or in previous" +
                "jobs. ")
    }
    onResponse{
        furhat.say("Text")
        goto(TeamRole)
    }
}

val TeamRole : State = state(Parent){
    onEntry{
        furhat.ask("How would you describe your role in a team project? What kind of responsibilities do you" +
                "usually take on?")
    }
    onResponse{
        furhat.say("Text")
        goto(EndSecIntro)
    }
}