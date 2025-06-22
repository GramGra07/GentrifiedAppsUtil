package org.gentrifiedApps.gentrifiedAppsUtil.hardware.motor

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

class DualEncoder {
    companion object {
        @JvmStatic
        fun newInstance(leftEncoder: DcMotor, rightEncoder: DcMotor): DualEncoder {
            return DualEncoder(leftEncoder, rightEncoder)
        }
    }

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

    constructor(leftName: String, rightName: String, hwMap: LinearOpMode) {
        this.leftEncoder = hwMap.hardwareMap.get(DcMotor::class.java, leftName)
        this.rightEncoder = hwMap.hardwareMap.get(DcMotor::class.java, rightName)
    }

    fun reset(): DualEncoder {
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

    fun getMost(): Int {
        return if (leftEncoder.currentPosition > rightEncoder.currentPosition) {
            leftEncoder.currentPosition
        } else {
            rightEncoder.currentPosition
        }
    }

    fun getLeast(): Int {
        return if (leftEncoder.currentPosition < rightEncoder.currentPosition) {
            leftEncoder.currentPosition
        } else {
            rightEncoder.currentPosition
        }
    }

    fun telemetry(telemetry: Telemetry) {
        telemetry.addData("Left Encoder", leftEncoder.currentPosition)
        telemetry.addData("Right Encoder", rightEncoder.currentPosition)
    }

    fun isAtTolerance(target: Double, tolerance: Double): Boolean {
        return (target - tolerance < getMost()) && (getMost() < target + tolerance)
    }
}