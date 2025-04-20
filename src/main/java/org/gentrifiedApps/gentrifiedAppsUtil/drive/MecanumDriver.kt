package org.gentrifiedApps.gentrifiedAppsUtil.drive

import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients


class MecanumDriver {
    companion object {
        /**
         * Drive mecanum.
         * @param x_ The x value of the controller. ex leftStickX
         * @param y_ The y value of the controller. ex leftStickY
         * @param rotation_ The rotation value of the controller. ex rightStickX
         * @return The coefficients to drive the robot.
         * @see DrivePowerCoefficients
         */
        @Suppress("LocalVariableName")
        @JvmStatic
        fun driveMecanum(
            x_: Float,
            y_: Float,
            rotation_: Float,
        ): DrivePowerCoefficients {
            val x = x_.toDouble()
            val y = y_.toDouble()
            val rotation = rotation_.toDouble()
            val frontLeftPower: Double = (y + x + rotation)
            val backLeftPower: Double = (y - x + rotation)
            val frontRightPower: Double = (y - x - rotation)
            val backRightPower: Double = (y + x - rotation)
            return DrivePowerCoefficients(
                frontLeftPower,
                frontRightPower,
                backLeftPower,
                backRightPower
            )
        }
    }
}