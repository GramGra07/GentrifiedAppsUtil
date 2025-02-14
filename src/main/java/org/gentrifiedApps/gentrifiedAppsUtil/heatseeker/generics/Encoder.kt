package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

//perpOffsetY and parOffsetX
class Encoder(
    private val encoderSpecs: EncoderSpecs,
    name: String,
    direction: DcMotorSimple.Direction,
    val offset: Double,
    hwMap: HardwareMap?
) {
    private var encoder: DcMotor?
    private var lastPosition = 0

    init {
        require(name.isNotBlank()) { "name must not be blank" }
        encoder = hwMap?.get(DcMotor::class.java, name)
        encoder?.direction = direction
        reset()
    }

    fun getTicks(): Int {
        return encoder?.currentPosition ?: 0
    }

    fun setLastPosition() {
        lastPosition = getTicks()
    }

    fun reset() {
        encoder?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        encoder?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lastPosition = getTicks()
    }

    fun getInches(): Double {
        return ticksToInches(getTicks())
    }

    fun getDelta(): Int {
        return getTicks() - lastPosition
    }

    private fun ticksToInches(ticks: Int): Double {
        return ticks.toDouble() / encoderSpecs.ticksPerInch
    }

    fun ticksPerIn(): Double {
        return encoderSpecs.ticksPerInch
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