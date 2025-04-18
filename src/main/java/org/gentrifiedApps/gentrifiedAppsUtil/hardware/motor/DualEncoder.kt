package org.gentrifiedApps.gentrifiedAppsUtil.hardware.motor

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class DualEncoder {
    private var leftEncoder: DcMotor
    private var rightEncoder: DcMotor

    constructor(leftEncoder: DcMotor, rightEncoder: DcMotor) {
        this.leftEncoder = leftEncoder
        this.rightEncoder = rightEncoder
    }
    constructor(leftName: String, rightName: String, hwMap: HardwareMap) {
        this.leftEncoder = hwMap.get(DcMotor::class.java, leftName)
        this.rightEncoder = hwMap.get(DcMotor::class.java, rightName)
    }
    constructor(leftName: String, rightName: String, hwMap: LinearOpMode){
        this.leftEncoder = hwMap.hardwareMap.get(DcMotor::class.java, leftName)
        this.rightEncoder = hwMap.hardwareMap.get(DcMotor::class.java, rightName)
    }
    fun reset(): DualEncoder{
        MotorExtensions.resetMotor(leftEncoder)
        MotorExtensions.resetMotor(rightEncoder)
        return this
    }
    fun getLeftPosition(): Int {
        return leftEncoder.currentPosition
    }
    fun getRightPosition(): Int {
        return rightEncoder.currentPosition
    }
    fun getAveragePosition(): Int {
        return (leftEncoder.currentPosition + rightEncoder.currentPosition) / 2
    }
    fun getMost(): Int{
        return if (leftEncoder.currentPosition > rightEncoder.currentPosition) {
            leftEncoder.currentPosition
        } else {
            rightEncoder.currentPosition
        }
    }
    fun getLeast(): Int{
        return if (leftEncoder.currentPosition < rightEncoder.currentPosition) {
            leftEncoder.currentPosition
        } else {
            rightEncoder.currentPosition
        }
    }
}