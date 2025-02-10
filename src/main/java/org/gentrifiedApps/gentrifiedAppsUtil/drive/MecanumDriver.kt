package org.gentrifiedApps.gentrifiedAppsUtil.drive

import android.R.attr


class MecanumDriver {
    companion object {
        /**
         * Drive mecanum.
         * @param x The x value of the controller. ex leftStickX
         * @param y The y value of the controller. ex leftStickY
         * @param rotation The rotation value of the controller. ex rightStickX
         * @return The coefficients to drive the robot.
         * @see DrivePowerCoefficients
         */
        @JvmStatic
        fun driveMecanum(
            x: Double,
            y: Double,
            rotation: Double,
        ): DrivePowerCoefficients {
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