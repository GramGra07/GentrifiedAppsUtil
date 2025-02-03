package org.gentrifiedApps.gentrifiedAppsUtil.gamepad

import com.qualcomm.robotcore.hardware.Gamepad

/**
 * A class to represent a pair of gamepads
 * It is a little more intensive to do it this way, but reduces function calls in your own code
 * @param gamepad1 The first gamepad
 * @param gamepad2 The second gamepad
 * @param loopSaver Whether to enable loop save mode (could reduce performance)
 */
class GamepadPair (gamepad1: Gamepad, gamepad2: Gamepad, val loopSaver: Boolean = false){
    val gamepad1Plus= GamepadPlus(gamepad1,loopSaver)
    val gamepad2Plus= GamepadPlus(gamepad2,loopSaver)

    /**
     * Syncs the gamepads, must be run at the **end** of your loop
     */
    fun sync(){
        gamepad1Plus.sync()
        gamepad2Plus.sync()
    }

    /**
     * This function is used to save the current state of the gamepad at the **BEGINNING** of your code
     */
    fun loopSavingRead() {
        require(loopSaver) { "Loop save mode is not enabled" }
        gamepad1Plus.loopSavingRead()
        gamepad2Plus.loopSavingRead()
    }
}