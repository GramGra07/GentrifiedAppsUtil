package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad

/**
 * Enum class for all boolean buttons on a gamepad
 */
enum class Button(val alias: Button? = null) {
    A,
    B,
    X,
    Y,
    CROSS(A),
    CIRCLE(B),
    TRIANGLE(Y),
    SQUARE(X),
    L1,
    LEFT_BUMPER(L1), // Alias for L1
    R1,
    RIGHT_BUMPER(R1), // Alias for R1
    SHARE,
    OPTIONS,
    TOUCHPAD,
    LEFT_STICK,
    RIGHT_STICK,
    DPAD_UP,
    DPAD_DOWN,
    DPAD_LEFT,
    DPAD_RIGHT;

    fun resolveAlias(): Button {
        return alias ?: this
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