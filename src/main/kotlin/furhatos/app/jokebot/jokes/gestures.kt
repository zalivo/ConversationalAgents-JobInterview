package furhatos.app.jokebot.jokes

import furhatos.gestures.ARKitParams
import furhatos.gestures.BasicParams
import furhatos.gestures.CharParams.*
import furhatos.gestures.defineGesture


/**
 * The gestures are defined here.
 */

val reset = defineGesture("reset") {
    frame(0.0) {
        // Reset all facial features to their default positions
        CHEEK_BONES_DOWN to 0.0
        CHEEK_BONES_NARROWER to 0.0
        CHEEK_BONES_UP to 0.0
        CHEEK_BONES_WIDER to 0.0
        CHEEK_FULLER to 0.0
        CHEEK_THINNER to 0.0
        CHIN_DOWN to 0.0
        CHIN_NARROWER to 0.0
        CHIN_UP to 0.0
        CHIN_WIDER to 0.0
        EYEBROW_DOWN to 0.0
        EYEBROW_LARGER to 0.0
        EYEBROW_NARROWER to 0.0
        EYEBROW_SMALLER to 0.0
        EYEBROW_TILT_DOWN to 0.0
        EYEBROW_TILT_UP to 0.0
        EYEBROW_UP to 0.0
        EYEBROW_WIDER to 0.0
        EYES_DOWN to 0.0
        EYES_NARROWER to 0.0
        EYES_SCALE_DOWN to 0.0
        EYES_SCALE_UP to 0.0
        EYES_TILT_DOWN to 0.0
        EYES_TILT_UP to 0.0
        EYES_UP to 0.0
        EYES_WIDER to 0.0
        MOUTH_DOWN to 0.0
        MOUTH_FLATTER to 0.0
        MOUTH_NARROWER to 0.0
        MOUTH_SCALE to 0.0
        MOUTH_UP to 0.0
        MOUTH_WIDER to 0.0
        NOSE_DOWN to 0.0
        NOSE_NARROWER to 0.0
        NOSE_UP to 0.0
        NOSE_WIDER to 0.0
        LIP_BOTTOM_THICKER to 0.0
        LIP_BOTTOM_THINNER to 0.0
        LIP_TOP_THICKER to 0.0
        LIP_TOP_THINNER to 0.0
    }
}

val welcome = defineGesture("welcome") {
    frame(0.10) {
        ARKitParams.JAW_LEFT to 1.0
        ARKitParams.MOUTH_DIMPLE_LEFT to 1.0
        BasicParams.NECK_PAN to -15
        reset(0.5)
    }
}

val curiosity = defineGesture("curiosity") {
    frame(0.1, 0.5) {
        EYES_WIDER to 0.15
        MOUTH_UP to 0.5
        ARKitParams.BROW_OUTER_UP_RIGHT to 0.5
        ARKitParams.JAW_FORWARD to 0.5
    }
    reset(2.0)
}






//JokeBot gestures
val indefiniteBigSmile = defineGesture {
    frame(0.32, 0.64, persist = true) {
        BasicParams.BROW_UP_LEFT to 1.0
        BasicParams.BROW_UP_RIGHT to 1.0
        BasicParams.SMILE_OPEN to 0.4
        BasicParams.SMILE_CLOSED to 0.7
    }
}

val indefiniteSmile = defineGesture {
    frame(0.32, 0.72, persist = true) {
        BasicParams.SMILE_CLOSED to 0.5
    }
    frame(0.2, 0.72){
        BasicParams.BROW_UP_LEFT to 1.0
        BasicParams.BROW_UP_RIGHT to 1.0
    }
    frame(0.16, 0.72){
        BasicParams.BLINK_LEFT to 0.1
        BasicParams.BLINK_RIGHT to 0.1
    }
}

//No more smiling
val stopSmile = defineGesture {
    frame(0.32, 0.64) {
        BasicParams.BROW_UP_LEFT to 0.0
        BasicParams.BROW_UP_RIGHT to 0.0
        BasicParams.SMILE_OPEN to 0.0
        BasicParams.SMILE_CLOSED to 0.0
    }
}