/***
 * In nlu.kt we can store words that we want the conversational agents to listen for
 */

package furhatos.app.jokebot.nlu

import furhatos.nlu.Intent
import furhatos.util.Language

/**
 * Do we need to keep the classes GoodJoke and BadJoke?
 */
class GoodJoke: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "Good one", "I like it", "good joke", "very funny", "hilarious", "haha", "great joke", "amazing"
        )
    }
}

class BadJoke: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "not funny", "not that funny", "not good", "not so good", "bad joke", "terrible"
        )
    }
}

//Intent for motivated user
//Answer to question "Why does this role interest you?"
class motivatedUser: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "I am looking forward to", "I am motivated"
        )
    }
}

class unMotivatedUser: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "I am interested because of the money", "Money", "Not interested", "Why would I be interested"
        )
    }
}

/**
 * This we could use when the user is asked where did he hear about this position.
 */
class socialMedia : Intent(){
    override fun getExamples(lang: Language): List<String>{
        return listOf(
        "Instagram", "TikTok", "Snapchat", "Twitter", "Facebook", "LinkedIn", "YouTube", "Pinterest", "Reddit", "Clubhouse", "Discord", "WhatsApp", "Telegram", "WeChat", "Twitch")
    }
}