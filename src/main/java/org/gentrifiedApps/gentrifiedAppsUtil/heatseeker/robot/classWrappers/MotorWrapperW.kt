package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple


class DcMotorW(name: String, port: Int) : FTCWrapper(name, port) {
    fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior?) {
        TODO("Not yet implemented")
    }

    fun getZeroPowerBehavior(): DcMotor.ZeroPowerBehavior? {
        TODO("Not yet implemented")
    }

    fun setTargetPosition(position: Int) {
        TODO("Not yet implemented")
    }

    fun getTargetPosition(): Int {
        TODO("Not yet implemented")
    }

    fun isBusy(): Boolean {
        TODO("Not yet implemented")
    }

    fun getCurrentPosition(): Int {
        TODO("Not yet implemented")
    }

    fun setMode(mode: DcMotor.RunMode?) {
        TODO("Not yet implemented")
    }

    fun getMode(): DcMotor.RunMode? {
        TODO("Not yet implemented")
    }

    fun setDirection(direction: DcMotorSimple.Direction?) {
        TODO("Not yet implemented")
    }

    fun getDirection(): DcMotorSimple.Direction? {
        TODO("Not yet implemented")
    }

    fun setPower(power: Double) {
        TODO("Not yet implemented")
    }

    fun getPower(): Double {
        TODO("Not yet implemented")
    }

    fun close() {
        TODO("Not yet implemented")
    }
}