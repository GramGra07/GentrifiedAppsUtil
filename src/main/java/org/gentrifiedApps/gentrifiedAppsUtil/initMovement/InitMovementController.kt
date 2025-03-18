package org.gentrifiedApps.gentrifiedAppsUtil.initMovement

import com.qualcomm.robotcore.hardware.Gamepad
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.GamepadPlus
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

/**
 * A class to check if the robot should move after running the robot
 * @constructor Can use Gamepad or GamepadPlus
 */
class InitMovementController {
    private var gamepadPlus: GamepadPlus
    private var gamepadPlus2: GamepadPlus

    /**
     * @param gamepad The first gamepad to use
     * @param gamepad2 The second gamepad to use
     */
    constructor(gamepad: Gamepad, gamepad2: Gamepad) {
        gamepadPlus = GamepadPlus(gamepad)
        gamepadPlus2 = GamepadPlus(gamepad2)
        resetHasMoved()
    }

    /**
     * @param gamepadPlus The first gamepad to use
     * @param gamepadPlus2 The second gamepad to use
     */
    constructor(gamepadPlus: GamepadPlus, gamepadPlus2: GamepadPlus) {
        this.gamepadPlus = gamepadPlus
        this.gamepadPlus2 = gamepadPlus2
        resetHasMoved()
    }

    private var hasMoved = false

    /**
     * @return Whether the robot has moved
     */
    fun hasMovedOnInit(): Boolean {
        return hasMoved
    }

    /**
     * Check if the robot has moved
     * @see hasMovedOnInit
     */
    fun checkHasMovedOnInit() {
        if (!hasMoved) {
            if ((!gamepadPlus.atRest() || !gamepadPlus2.atRest())) {
                hasMoved = true
                Scribe.instance.setSet("IMC").logDebug("Robot has moved")
            }
        }
    }

    fun resetHasMoved() {
        hasMoved = false
    }
}