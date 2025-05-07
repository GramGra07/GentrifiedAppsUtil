package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad

import com.qualcomm.robotcore.hardware.Gamepad

/**
 * A class to represent a pair of gamepads
 * It is a little more intensive to do it this way, but reduces function calls in your own code
 * @param gamepad1 The first gamepad
 * @param gamepad2 The second gamepad
 * @param loopSaver Whether to enable loop save mode (could reduce performance)
 */
class GamepadPair(gamepad1: Gamepad, gamepad2: Gamepad) {
    companion object {
        @JvmStatic
        fun newInstance(gamepad1: Gamepad, gamepad2: Gamepad): GamepadPair {
            return GamepadPair(gamepad1, gamepad2)
        }
    }

    val gamepad1Plus = GamepadPlus(gamepad1)
    val gamepad2Plus = GamepadPlus(gamepad2)

    /**
     * Syncs the gamepads, must be run at the **end** of your loop
     */
    fun sync() {
        gamepad1Plus.sync()
        gamepad2Plus.sync()
    }
}