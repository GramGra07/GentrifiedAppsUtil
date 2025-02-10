package org.gentrifiedApps.gentrifiedAppsUtil.drive

import com.qualcomm.robotcore.util.Range
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class FieldCentricDriver {
    companion object {
        /**
         * Drive field centrically.
         * @param x The x value of the controller. ex leftStickX
         * @param y The y value of the controller. ex leftStickY
         * @param rotation The rotation value of the controller. ex rightStickX
         * @param gyroAngle The angle of the gyro. ex imu.getAngularOrientation().firstAngle
         * @return The coefficients to drive the robot.
         * @see DrivePowerCoefficients
         */
        @JvmStatic
        fun driveFieldCentric(
            x: Double,
            y: Double,
            rotation: Double,
            gyroAngle: Double
        ): DrivePowerCoefficients {
            val controllerAngle = Math.toDegrees(atan2(y, x))
            val robotDegree = Math.toDegrees(gyroAngle)
            val movementDegree = controllerAngle - robotDegree
            val gamepadHypot = Range.clip(hypot(x, y), 0.0, 1.0)
            val yControl = cos(Math.toRadians(movementDegree)) * gamepadHypot
            val xControl = (sin(Math.toRadians(movementDegree)) * gamepadHypot)
            val frontRightPower =
                (yControl * abs(yControl) - xControl * abs(xControl) + rotation)
            val backRightPower =
                (yControl * abs(yControl) + xControl * abs(xControl) + rotation)
            val frontLeftPower =
                (yControl * abs(yControl) + xControl * abs(xControl) - rotation)
            val backLeftPower =
                (yControl * abs(yControl) - xControl * abs(xControl) - rotation)
            return DrivePowerCoefficients(
                frontLeftPower,
                frontRightPower,
                backLeftPower,
                backRightPower
            )
        }
    }
}