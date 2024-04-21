  package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*

val startConsent : State = state(Parent) {
    /**
     * Robot Johnny welcomes user to job interview and asks for user's name
     */
    onEntry {
        furhat.ask("Hi there. Welcome to this job interview for the IT junior position at our company. My name is Johnny," +
                "and I am going to lead this interview. Before we start with the interview I would like to say," +
                "that this interview is going to be recorded and sent to our HR team for further evaluation." +
                "Do you give consent for this interview to be recorded?")
    }
    onResponse {
        furhat.say("Text")
        goto(askName)
    }
}

val askName : State = state(Parent) {

    onEntry{
        furhat.ask("Perfect, what is your name?")
    }
    /**
     * Says "Nice to meet you *user name*
     */
    onResponse<PersonName> {
        furhat.say("Nice to meet you " + it.intent)
        goto(canWeStart)
    }

    /**
     * If the user says something else or the name is not in library
     */
    onResponse {
        furhat.say("Nice to meet you!")
        goto(canWeStart)
    }

    /**
     * If the user does not respond, proceed anyway
     */
    onNoResponse {
        //Add a funny comment
        goto(canWeStart)
    }
}

  /**
   * Starting the interview state
   */
  val canWeStart: State = state(Parent) {

    onEntry {
        furhat.ask("Are you comfortable and ready to start the interview?", timeout = 4000)
    }

      /**
       * If user responds yes, proceed to tell me about yourself question
       */
    onResponse<Yes> {
        furhat.say("Perfect! We will start then.")
        goto(tellMeAboutYourself)
    }

      /**
       * If user says no, proceed anyway
       */
    onResponse<No> {
        furhat.say("Okay well that's a shame, we really need to start though.")
        goto(tellMeAboutYourself)
    }

      /**
       * If user responds something else, proceed anyway
       */
    onResponse {
        furhat.say("I will take that as a yes.")
        goto(tellMeAboutYourself)
    }

      /**
       * If user doesn't respond, proceed anyway
       */
    onNoResponse {
        furhat.say("I will take that as a yes.")
        goto(tellMeAboutYourself)
    }
}

  /**
   * Tell me about yourself question, user gets to answer for 1 minute
   * Maybe add something in case the users asks why? I dunno, just an idea
   * Even if I say something short, the CA automatically jumps into the conversation
    */

val tellMeAboutYourself: State = state(Parent) {
    onEntry {
        furhat.ask("So, tell me a little bit about yourself.", timeout = 60000)
    }

      /**
       * User responds something, proceed
       */
    onResponse {
        furhat.say("That sounds really interesting!")
        goto(roleInterest)
    }

      /**
       * User doesn't respond, the user should repeat oneself
       */
    onNoResponse{
        furhat.say("I can't really hear you. Could you repeat that?")
        goto(repeatAboutYourself)
    }
}

  /**
   * For repetition of tell me about yourself question
   */
  val repeatAboutYourself: State = state(Parent) {
    onEntry {
        furhat.listen(timeout = 60000)
    }

      /**
       * User answers something, proceed
       */
    onResponse{
        furhat.say("That sounds really interesting!")
        goto(roleInterest)
    }

    /**
     * User doesn't respond, proceed anyway
     */
    onNoResponse{
        furhat.say("I couldn't hear you now either. Could you repeat that?")
        //We re-enter the repeating, robot will listen again
        //User can then repeat their answer
        reentry()
    }
    }

