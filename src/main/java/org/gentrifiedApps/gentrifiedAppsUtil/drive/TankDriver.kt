package org.gentrifiedApps.gentrifiedAppsUtil.drive

import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients


class TankDriver {
    companion object {
        /**
         * Drive tank with left and right values, left controls the left side of the robot and right controls the right side.
         * @param l The left value of the controller. ex leftStickY
         * @param r The right value of the controller. ex rightStickY
         * @return The coefficients to drive the robot.
         * @see DrivePowerCoefficients
         */
        @JvmStatic
        fun driveTank(
            l: Double,
            r: Double
        ): DrivePowerCoefficients {
            val frontLeftPower: Double = l
            val backLeftPower: Double = l
            val frontRightPower: Double = r
            val backRightPower: Double = r
            return DrivePowerCoefficients(
                frontLeftPower,
                frontRightPower,
                backLeftPower,
                backRightPower
            )
        }

        /**
         * Drive tank with
         * @param drivePower The power to drive the robot.
         * @param rotation The power to rotate the robot.
         * @return The coefficients to drive the robot.
         * @see DrivePowerCoefficients
         */
        @JvmStatic
        fun driveTankRobotCentric(
            drivePower: Double,
            rotation: Double,
        ): DrivePowerCoefficients {
            val frontLeftPower: Double = drivePower - rotation
            val backLeftPower: Double = drivePower - rotation
            val frontRightPower: Double = drivePower + rotation
            val backRightPower: Double = drivePower + rotation
            return DrivePowerCoefficients(
                frontLeftPower,
                frontRightPower,
                backLeftPower,
                backRightPower
            )
        }
    }
}