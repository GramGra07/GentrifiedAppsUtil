package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles

internal class IMUW : IMU {

    override fun getManufacturer(): HardwareDevice.Manufacturer? {
        TODO("Not yet implemented")
    }

    override fun getDeviceName(): String? {
        TODO("Not yet implemented")
    }

    override fun getConnectionInfo(): String? {
        TODO("Not yet implemented")
    }

    override fun getVersion(): Int {
        TODO("Not yet implemented")
    }

    override fun resetDeviceConfigurationForOpMode() {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun initialize(parameters: IMU.Parameters?): Boolean {
        TODO("Not yet implemented")
    }

    override fun resetYaw() {
        TODO("Not yet implemented")
    }

    override fun getRobotYawPitchRollAngles(): YawPitchRollAngles? {
        TODO("Not yet implemented")
    }

    override fun getRobotOrientation(
        reference: AxesReference?,
        order: AxesOrder?,
        angleUnit: AngleUnit?
    ): Orientation? {
        TODO("Not yet implemented")
    }

    override fun getRobotOrientationAsQuaternion(): Quaternion? {
        TODO("Not yet implemented")
    }

    override fun getRobotAngularVelocity(angleUnit: AngleUnit?): AngularVelocity? {
        TODO("Not yet implemented")
    }
}