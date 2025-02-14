package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class Encoder(
    private val encoderSpecs: EncoderSpecs,
    name: String,
    direction: DcMotorSimple.Direction,
    hwMap: HardwareMap?
) {
    private var encoder: DcMotor?

    init {
        require(name.isNotBlank()) { "name must not be blank" }
        encoder = hwMap?.get(DcMotor::class.java, name)
        encoder?.direction = direction
        encoder?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        encoder?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

    }

    fun getTicks(): Int {
        return encoder?.currentPosition ?: 0
    }

    fun getInches(): Double {
        return ticksToInches(getTicks())
    }

    fun getDelta(last: Int): Int {
        return getTicks() - last
    }

    private fun ticksToInches(ticks: Int): Double {
        return ticks.toDouble() / encoderSpecs.ticksPerInch
    }
}

data class EncoderSpecs(
    val ticksPerRev: Int,
    val wheelDiameter: Double,
    val gearRatio: Double = 1.0,
) {
    val ticksPerInch = (ticksPerRev * gearRatio) / (wheelDiameter * Math.PI)

    init {
        require(ticksPerRev > 0) { "ticksPerRev must be greater than 0" }
        require(wheelDiameter > 0) { "wheelDiameter must be greater than 0" }
        require(gearRatio > 0) { "gearRatio must be greater than 0" }
    }
}