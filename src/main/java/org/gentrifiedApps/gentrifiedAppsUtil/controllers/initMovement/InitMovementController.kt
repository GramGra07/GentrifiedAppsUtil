package org.gentrifiedApps.gentrifiedAppsUtil.controllers.initMovement

import com.qualcomm.robotcore.hardware.Gamepad
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

/**
 * A class to check if the robot should move after running the robot
 * @constructor Can use Gamepad or GamepadPlus
 */
class InitMovementController(val gamepad1: Gamepad, val gamepad2: Gamepad) {
    init {
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
            if ((!gamepad1.atRest() || !gamepad2.atRest())) {
                hasMoved = true
                Scribe.instance.setSet("IMC").logDebug("Robot has moved")
            }
        }
    }

    fun resetHasMoved() {
        hasMoved = false
    }
}