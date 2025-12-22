package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorController
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType


class DcMotorW(name: String, port: Int) : DcMotor, FTCWrapper(name, port) {
    private var zpb: DcMotor.ZeroPowerBehavior? = null
    override fun getMotorType(): MotorConfigurationType? {
        TODO("Not yet implemented")
    }

    override fun setMotorType(motorType: MotorConfigurationType?) {
        TODO("Not yet implemented")
    }

    override fun getController(): DcMotorController? {
        TODO("Not yet implemented")
    }

    override fun getPortNumber(): Int {
        TODO("Not yet implemented")
    }

    override fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior?) {
        zpb = zeroPowerBehavior
    }

    override fun getZeroPowerBehavior(): DcMotor.ZeroPowerBehavior? {
        return zpb
    }

    override fun setPowerFloat() {
        TODO("Not yet implemented")
    }

    override fun getPowerFloat(): Boolean {
        TODO("Not yet implemented")
    }

    var tp: Int = 0
    override fun setTargetPosition(position: Int) {
        tp = position
    }

    override fun getTargetPosition(): Int {
        return tp
    }

    override fun isBusy(): Boolean {
        TODO("Not yet implemented")
    }

    private var currentPose = 0
    override fun getCurrentPosition(): Int {
        return currentPose
    }

    private var m: DcMotor.RunMode? = null

    override fun setMode(mode: DcMotor.RunMode?) {
        m = mode
        if (m == DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
            currentPose = 0
        }
    }

    override fun getMode(): DcMotor.RunMode? {
        return m
    }

    private var dir: DcMotorSimple.Direction? = DcMotorSimple.Direction.FORWARD

    override fun setDirection(direction: DcMotorSimple.Direction?) {
        dir = direction
    }

    override fun getDirection(): DcMotorSimple.Direction? {
        return dir
    }

    private var power = 0.0
    override fun setPower(power: Double) {
        // also should send current pose and make that increment
        this.power = power
        currentPose += (power * 10).toInt()
    }

    override fun getPower(): Double {
        return this.power
    }

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
}