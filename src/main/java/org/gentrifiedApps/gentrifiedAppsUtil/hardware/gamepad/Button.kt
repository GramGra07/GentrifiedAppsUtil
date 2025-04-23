package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad

/**
 * Enum class for all boolean buttons on a gamepad
 */
enum class Button {
    A,
    B,
    X,
    Y,
    CROSS,
    CIRCLE,
    TRIANGLE,
    SQUARE,
    L1,
    R1,
    SHARE,
    OPTIONS,
    TOUCHPAD,
    LEFT_STICK,
    RIGHT_STICK,
    DPAD_UP,
    DPAD_DOWN,
    DPAD_LEFT,
    DPAD_RIGHT,
    RIGHT_BUMPER,
    LEFT_BUMPER;
    companion object {
        @JvmStatic
        fun aToPS(): Button {
            return CROSS
        }
        @JvmStatic
        fun bToPS(): Button {
            return CIRCLE
        }
        @JvmStatic
        fun xToPS(): Button {
            return SQUARE
        }
        @JvmStatic
        fun yToPS(): Button {
            return TRIANGLE
        }
        @JvmStatic
        fun triangleToX(): Button {
            return Y
        }
        @JvmStatic
        fun squareToX(): Button {
            return X
        }
        @JvmStatic
        fun circleToX(): Button {
            return B
        }
        @JvmStatic
        fun crossToX(): Button {
            return A
        }
    }
}

/**
 * Enum class for all float buttons on a gamepad
 */
enum class FloatButton {
    LEFT_X,
    LEFT_Y,
    RIGHT_X,
    RIGHT_Y,
    LEFT_TRIGGER,
    RIGHT_TRIGGER,
    TOUCH_X,
    TOUCH_Y,
}