package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.motor

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorController
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import org.gentrifiedApps.gentrifiedAppsUtil.controllers.PIDFController

class PIDMotor: DcMotor {
    constructor(p: Double, i: Double, d: Double, f: Double) {
        this.pidController.setPIDF(p, i, d, f)
    }
    private var pidController : PIDFController = PIDFController()
    private var target = 0.0

    fun setTarget(target: Double) {
        this.target = target
    }
    fun getTarget(): Double {
        return this.target
    }
    fun setP(p: Double) {
        this.pidController.kP
    }
    fun setI(i: Double) {
        this.pidController.kI
    }
    fun setD(d: Double) {
        this.pidController.kD
    }
    fun setF(f: Double) {
        this.pidController.kF
    }

    fun setPIDF(p: Double, i: Double, d: Double, f: Double) {
        this.pidController.setPIDF(p, i, d, f)
    }

    fun setPIDPower() {
        this.power = this.pidController.calculate(target, this.currentPosition.toDouble())
    }

    override fun getMotorType(): MotorConfigurationType? {
        return this.motorType
    }

    override fun setMotorType(motorType: MotorConfigurationType?) {
        this.motorType = motorType
    }

    override fun getController(): DcMotorController? {
        return this.controller
    }

    override fun getPortNumber(): Int {
        return this.portNumber
    }

    override fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior?) {
        this.zeroPowerBehavior = zeroPowerBehavior
    }

    override fun getZeroPowerBehavior(): DcMotor.ZeroPowerBehavior? {
        return this.zeroPowerBehavior
    }

    override fun setPowerFloat() {
        this.setPowerFloat()
    }

    override fun getPowerFloat(): Boolean {
        return this.powerFloat
    }

    override fun setTargetPosition(position: Int) {
        this.targetPosition = position
    }

    override fun getTargetPosition(): Int {
        return this.targetPosition
    }

    override fun isBusy(): Boolean {
        return this.isBusy
    }

    override fun getCurrentPosition(): Int {
        return this.currentPosition
    }

    override fun setMode(mode: DcMotor.RunMode?) {
        this.mode = mode
    }

    override fun getMode(): DcMotor.RunMode? {
        return this.mode
    }

    override fun setDirection(direction: DcMotorSimple.Direction?) {
        this.direction = direction
    }

    override fun getDirection(): DcMotorSimple.Direction? {
        return this.direction
    }

    override fun setPower(power: Double) {
        this.power = power
    }

    override fun getPower(): Double {
        return this.power
    }

    override fun getManufacturer(): HardwareDevice.Manufacturer? {
        return this.manufacturer
    }

    override fun getDeviceName(): String? {
        return this.deviceName
    }

    override fun getConnectionInfo(): String? {
        return this.connectionInfo
    }

    override fun getVersion(): Int {
        return this.version
    }

    override fun resetDeviceConfigurationForOpMode() {
        this.resetDeviceConfigurationForOpMode()
    }

    override fun close() {
        this.close()
    }
}