package org.gentrifiedApps.gentrifiedAppsUtil.drive

import com.qualcomm.robotcore.util.Range
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.clip
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.AngleUnit
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
         * @param gyroAngle The angle of the gyro. ex imu.getAngularOrientation().firstAngle in **radians**
         * @param offset The offset to add to the gyro angle. ex Angle(90.0, AngleUnit.DEGREES)
         * @return The coefficients to drive the robot.
         * @see DrivePowerCoefficients
         */
        @JvmStatic
        fun driveFieldCentric(
            x: Double,
            y: Double,
            rotation: Double,
            gyroAngle: Angle,
            offset: Angle
        ): DrivePowerCoefficients {
            val controllerAngle = atan2(y, x)
            val robotAngle = gyroAngle.toRadians() + offset.toRadians()
            val movementAngle = controllerAngle - robotAngle
            val gamepadHypot = clip(hypot(x, y), 0.0, 1.0)
            val yControl = cos(movementAngle) * gamepadHypot
            val xControl = sin(movementAngle) * gamepadHypot
            val frontRightPower = yControl * abs(yControl) - xControl * abs(xControl) + rotation
            val backRightPower = yControl * abs(yControl) + xControl * abs(xControl) + rotation
            val frontLeftPower = yControl * abs(yControl) + xControl * abs(xControl) - rotation
            val backLeftPower = yControl * abs(yControl) - xControl * abs(xControl) - rotation
            return DrivePowerCoefficients(
                frontLeftPower,
                frontRightPower,
                backLeftPower,
                backRightPower
            )
        }

        /**
         * Overloaded method to drive field centrically with a default offset of 90 degrees.
         * @param x The x value of the controller. ex leftStickX
         * @param y The y value of the controller. ex leftStickY
         * @param rotation The rotation value of the controller. ex rightStickX
         * @param gyroAngle The angle of the gyro. ex imu.getAngularOrientation().firstAngle in **radians**
         * @return The coefficients to drive the robot.
         * @see DrivePowerCoefficients
         */
        @JvmStatic
        fun driveFieldCentric(
            x: Double, y: Double, rotation: Double, gyroAngle: Angle
        ): DrivePowerCoefficients {
            return driveFieldCentric(x, y, rotation, gyroAngle, Angle(90.0, AngleUnit.DEGREES))
        }
    }
}