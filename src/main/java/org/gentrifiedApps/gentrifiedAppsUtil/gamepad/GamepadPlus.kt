package org.gentrifiedApps.gentrifiedAppsUtil.gamepad

import com.qualcomm.robotcore.hardware.Gamepad
import java.util.EnumMap

/**
 * A class to extend the Gamepad features with more functionality
 * @param gamepad The gamepad to use
 * @param loopSaveMode Whether to enable loop save mode (could reduce performance)
 * @throws IllegalArgumentException If loopSaveMode is not enabled and you call loopSavingRead
 */
class GamepadPlus(gamepad: Gamepad, private val loopSaveMode:Boolean = false) {
    private var gamepad:Gamepad? = null

    init {
        this.gamepad = gamepad
        primarySync()
    }
    private var secondaryHash = EnumMap<Button, Boolean>(Button::class.java)
    private var hash = EnumMap<Button, Boolean>(Button::class.java)
    /**
    * This function is used to save the current state of the gamepad at the **BEGINNING** of your code
     **/
    fun loopSavingRead(){
        require(loopSaveMode){"Loop save mode is not enabled"}
        secondaryHash[Button.CROSS] = gamepad!!.cross
        secondaryHash[Button.CIRCLE] = gamepad!!.circle
        secondaryHash[Button.TRIANGLE] = gamepad!!.triangle
        secondaryHash[Button.SQUARE] = gamepad!!.square
        secondaryHash[Button.L1] = gamepad!!.left_bumper
        secondaryHash[Button.R1] = gamepad!!.right_bumper
        secondaryHash[Button.SHARE] = gamepad!!.back
        secondaryHash[Button.OPTIONS] = gamepad!!.start
        secondaryHash[Button.TOUCHPAD] = gamepad!!.guide
        secondaryHash[Button.LEFT_STICK] = gamepad!!.left_stick_button
        secondaryHash[Button.RIGHT_STICK] = gamepad!!.right_stick_button
        secondaryHash[Button.DPAD_UP] = gamepad!!.dpad_up
        secondaryHash[Button.DPAD_DOWN] = gamepad!!.dpad_down
        secondaryHash[Button.DPAD_LEFT] = gamepad!!.dpad_left
        secondaryHash[Button.DPAD_RIGHT] = gamepad!!.dpad_right
        secondaryHash[Button.RIGHT_BUMPER] = gamepad!!.right_bumper
        secondaryHash[Button.LEFT_BUMPER] = gamepad!!.left_bumper
    }
    private fun primarySync(){
        hash.clear()
        for (entry in Button.entries) {
            hash[entry] = false
        }
        sync()
        //
        if (loopSaveMode){
            secondaryHash.clear()
            for (entry in Button.entries) {
                secondaryHash[entry] = false
            }
            loopSavingRead()
        }
    }

    /**
     * This function is used to sync the gamepad at the **END** of your program
     */
    fun sync() {
        hash[Button.CROSS] = gamepad!!.cross
        hash[Button.CIRCLE] = gamepad!!.circle
        hash[Button.TRIANGLE] = gamepad!!.triangle
        hash[Button.SQUARE] = gamepad!!.square
        hash[Button.L1] = gamepad!!.left_bumper
        hash[Button.R1] = gamepad!!.right_bumper
        hash[Button.SHARE] = gamepad!!.back
        hash[Button.OPTIONS] = gamepad!!.start
        hash[Button.TOUCHPAD] = gamepad!!.guide
        hash[Button.LEFT_STICK] = gamepad!!.left_stick_button
        hash[Button.RIGHT_STICK] = gamepad!!.right_stick_button
        hash[Button.DPAD_UP] = gamepad!!.dpad_up
        hash[Button.DPAD_DOWN] = gamepad!!.dpad_down
        hash[Button.DPAD_LEFT] = gamepad!!.dpad_left
        hash[Button.DPAD_RIGHT] = gamepad!!.dpad_right
        hash[Button.RIGHT_BUMPER] = gamepad!!.right_bumper
        hash[Button.LEFT_BUMPER] = gamepad!!.left_bumper
    }
    private fun readBooleanButtonFromHash(button: Button): Boolean {
        return hash[button]!!
    }
    fun readBooleanButtonFromController(button: Button): Boolean {
        return if (loopSaveMode) secondaryHash[button]!! else returnButton(button)
    }
    private fun returnButton(button: Button): Boolean {
        return when (button) {
            Button.CROSS -> gamepad!!.cross
            Button.CIRCLE -> gamepad!!.circle
            Button.TRIANGLE -> gamepad!!.triangle
            Button.SQUARE -> gamepad!!.square
            Button.L1 -> gamepad!!.left_bumper
            Button.R1 -> gamepad!!.right_bumper
            Button.SHARE -> gamepad!!.back
            Button.OPTIONS -> gamepad!!.start
            Button.TOUCHPAD -> gamepad!!.guide
            Button.LEFT_STICK -> gamepad!!.left_stick_button
            Button.RIGHT_STICK -> gamepad!!.right_stick_button
            Button.DPAD_UP -> gamepad!!.dpad_up
            Button.DPAD_DOWN -> gamepad!!.dpad_down
            Button.DPAD_LEFT -> gamepad!!.dpad_left
            Button.DPAD_RIGHT -> gamepad!!.dpad_right
            Button.RIGHT_BUMPER -> gamepad!!.right_bumper
            Button.LEFT_BUMPER -> gamepad!!.left_bumper
        }
    }
    fun buttonJustPressed(button: Button): Boolean {
        return !readBooleanButtonFromHash(button) && readBooleanButtonFromController(button)
    }
    fun buttonJustReleased(button: Button): Boolean {
        return readBooleanButtonFromHash(button) && !readBooleanButtonFromController(button)
    }
    fun buttonPressed(button: Button): Boolean {
        return readBooleanButtonFromController(button)
    }
    fun buttonReleased(button: Button): Boolean {
        return !readBooleanButtonFromController(button)
    }
    fun buttonHeld(button: Button): Boolean {
        return readBooleanButtonFromHash(button) && readBooleanButtonFromController(button)
    }
    fun buttonNotHeld(button: Button): Boolean {
        return !readBooleanButtonFromHash(button) && !readBooleanButtonFromController(button)
    }
    fun readFloat(button: FloatButton): Float {
        return when (button) {
            FloatButton.LEFT_X -> gamepad!!.left_stick_x
            FloatButton.LEFT_Y -> gamepad!!.left_stick_y
            FloatButton.RIGHT_X -> gamepad!!.right_stick_x
            FloatButton.RIGHT_Y -> gamepad!!.right_stick_y
            FloatButton.LEFT_TRIGGER -> gamepad!!.left_trigger
            FloatButton.RIGHT_TRIGGER -> gamepad!!.right_trigger
            FloatButton.TOUCH_X -> gamepad!!.touchpad_finger_1_x
            FloatButton.TOUCH_Y -> gamepad!!.touchpad_finger_1_y
        }
    }
}