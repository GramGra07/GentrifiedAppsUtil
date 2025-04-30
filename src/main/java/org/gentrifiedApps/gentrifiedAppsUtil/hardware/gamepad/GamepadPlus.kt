package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad

import com.qualcomm.robotcore.hardware.Gamepad

class GamepadPlus(private val gamepad: Gamepad) {

    private val currentState = mutableMapOf<Button, Boolean>()
    private val previousState = mutableMapOf<Button, Boolean>()

    init {
        initializeButtonStates()
    }

    private fun initializeButtonStates() {
        Button.values().forEach { button ->
            currentState[button] = false
            previousState[button] = false
        }
        updateButtonStates()
    }

    fun updateButtonStates() {
        Button.values().forEach { button ->
            previousState[button] = currentState[button] ?: false
            currentState[button] = readButtonState(button)
        }
    }

    fun sync() {
        updateButtonStates()
    }

    private fun readButtonState(button: Button): Boolean {
        val resolvedButton = button.resolveAlias()
        return when (resolvedButton) {
            Button.A, Button.CROSS -> gamepad.cross
            Button.B, Button.CIRCLE -> gamepad.circle
            Button.X, Button.SQUARE -> gamepad.square
            Button.Y, Button.TRIANGLE -> gamepad.triangle
            Button.L1, Button.LEFT_BUMPER -> gamepad.left_bumper
            Button.R1, Button.RIGHT_BUMPER -> gamepad.right_bumper
            Button.SHARE -> gamepad.back
            Button.OPTIONS -> gamepad.start
            Button.TOUCHPAD -> gamepad.guide
            Button.LEFT_STICK -> gamepad.left_stick_button
            Button.RIGHT_STICK -> gamepad.right_stick_button
            Button.DPAD_UP -> gamepad.dpad_up
            Button.DPAD_DOWN -> gamepad.dpad_down
            Button.DPAD_LEFT -> gamepad.dpad_left
            Button.DPAD_RIGHT -> gamepad.dpad_right
        }
    }

    fun buttonJustPressed(button: Button): Boolean {
        return previousState[button] != true && (currentState[button] == true)
    }

    fun buttonJustReleased(button: Button): Boolean {
        return (previousState[button] ?: false) && !(currentState[button] ?: false)
    }

    fun buttonPressed(button: Button): Boolean {
        return currentState[button] ?: false
    }

    fun buttonReleased(button: Button): Boolean {
        return !(currentState[button] ?: false)
    }

    fun getButtonsCurrentlyPressed(): List<Button> {
        return currentState.filter { it.value }.keys.toList()
    }

    fun readFloat(button: FloatButton): Float {
        return when (button) {
            FloatButton.LEFT_X -> gamepad.left_stick_x
            FloatButton.LEFT_Y -> gamepad.left_stick_y
            FloatButton.RIGHT_X -> gamepad.right_stick_x
            FloatButton.RIGHT_Y -> gamepad.right_stick_y
            FloatButton.LEFT_TRIGGER -> gamepad.left_trigger
            FloatButton.RIGHT_TRIGGER -> gamepad.right_trigger
            FloatButton.TOUCH_X -> gamepad.touchpad_finger_1_x
            FloatButton.TOUCH_Y -> gamepad.touchpad_finger_1_y
        }
    }

    fun atRest(): Boolean {
        return gamepad.atRest()
    }
}