package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.motor

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER
import com.qualcomm.robotcore.hardware.DcMotorController
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import org.gentrifiedApps.gentrifiedAppsUtil.classes.pid.PIDFCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.controllers.PIDFController

class PIDMotor @JvmOverloads constructor(hwMap: HardwareMap,name: String,direction: DcMotorSimple.Direction? = DcMotorSimple.Direction.FORWARD,p: Double =0.0, i: Double=.0, d: Double=.0, f: Double=.0): DcMotor {
    private var motor : DcMotor = hwMap.get(DcMotor::class.java, name)

    constructor(hwMap: HardwareMap,name: String, p: Double, i: Double, d: Double, f: Double): this(hwMap, name, DcMotorSimple.Direction.FORWARD, p, i, d, f)
    constructor(hwMap: HardwareMap,name: String, pidfCoefficients: PIDFCoefficients): this(hwMap, name){
        this.setPIDF(pidfCoefficients.kP, pidfCoefficients.kI, pidfCoefficients.kD, pidfCoefficients.kF)
    }

    private var currentReversed = false
    private var currentReverseVal = 1
    private var pidController : PIDFController = PIDFController()
    private var target = 0.0
    init {
        this.setPIDF(p, i, d, f)
        this.direction = direction
    }
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
    fun reset(){
        this.target = 0.0
        this.mode = STOP_AND_RESET_ENCODER
        this.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }
    fun currentReversed(){
        if (currentReversed){
            currentReversed = false
            currentReverseVal = 1
        } else {
            currentReversed = true
            currentReverseVal = -1
        }
    }

    fun setPIDF(p: Double, i: Double, d: Double, f: Double) {
        this.pidController.setPIDF(p, i, d, f)
    }

    fun setPIDPower() {
        motor.power = this.pidController.calculate(target, motor.currentPosition.toDouble()*currentReverseVal)
    }

    override fun getMotorType(): MotorConfigurationType? {
        return motor.motorType
    }

    override fun setMotorType(motorType: MotorConfigurationType?) {
        motor.motorType = motorType
    }

    override fun getController(): DcMotorController? {
        return motor.controller
    }

    override fun getPortNumber(): Int {
        return motor.portNumber
    }

    override fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior?) {
        motor.zeroPowerBehavior = zeroPowerBehavior
    }

    override fun getZeroPowerBehavior(): DcMotor.ZeroPowerBehavior? {
        return motor.zeroPowerBehavior
    }

    override fun setPowerFloat() {
        motor.setPowerFloat()
    }

    override fun getPowerFloat(): Boolean {
        return motor.powerFloat
    }

    /**
     * DONT USE
     */
    override fun setTargetPosition(position: Int) {
        motor.targetPosition = position
    }

    override fun getTargetPosition(): Int {
        return motor.targetPosition
    }

    override fun isBusy(): Boolean {
        return motor.isBusy
    }

    override fun getCurrentPosition(): Int {
        return motor.currentPosition
    }

    override fun setMode(mode: DcMotor.RunMode?) {
        motor.mode = mode
    }

    override fun getMode(): DcMotor.RunMode? {
        return motor.mode
    }

    override fun setDirection(direction: DcMotorSimple.Direction?) {
        motor.direction = direction
    }

    override fun getDirection(): DcMotorSimple.Direction? {
        return motor.direction
    }

    override fun setPower(power: Double) {
        motor.power = power
    }

    override fun getPower(): Double {
        return motor.power
    }

    override fun getManufacturer(): HardwareDevice.Manufacturer? {
        return motor.manufacturer
    }

    override fun getDeviceName(): String? {
        return motor.deviceName
    }

    override fun getConnectionInfo(): String? {
        return motor.connectionInfo
    }

    override fun getVersion(): Int {
        return motor.version
    }

    override fun resetDeviceConfigurationForOpMode() {
        motor.resetDeviceConfigurationForOpMode()
    }

    override fun close() {
        motor.close()
    }
}