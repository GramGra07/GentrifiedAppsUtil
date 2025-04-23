package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

//perpOffsetY and parOffsetX
class Encoder(
    private val encoderSpecs: EncoderSpecs,
    name: String,
    direction: DcMotorSimple.Direction,
    private val hardReverse: Boolean,
    val offset: Double,
    hwMap: HardwareMap?
) {
    constructor(
        encoderSpecs: EncoderSpecs,
        name: String,
        direction: DcMotorSimple.Direction,
        hardReverse: Boolean,
        hwMap: HardwareMap?
    ) :
            this(encoderSpecs, name, direction, hardReverse, 0.0, hwMap)

    constructor(
        encoderSpecs: EncoderSpecs,
        name: String,
        direction: DcMotorSimple.Direction,
        hwMap: HardwareMap?
    ) :
            this(encoderSpecs, name, direction, false, 0.0, hwMap)

    private var encoder: DcMotor?
    private var lastPosition = 0

    init {
        require(name.isNotBlank()) { "name must not be blank" }
        encoder = hwMap?.get(DcMotor::class.java, name)
        encoder?.direction = direction
        reset()
    }

    fun getTicks(): Int {
        if (hardReverse) {
            return encoder?.currentPosition?.times(-1) ?: 0
        }
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

    fun getDeltaInches(): Double {
        return ticksToInches(getDelta())
    }

    private fun ticksToInches(ticks: Int): Double {
        return ticks.toDouble() / encoderSpecs.ticksPerInch
    }

    fun ticksPerIn(): Double {
        return encoderSpecs.ticksPerInch
    }
}

data class EncoderSpecs(
    private val ticksPerRev: Int,
    private val wheelDiameter: Double,
    private val gearRatio: Double = 1.0,
    var ticksPerInch: Double
) {
    constructor(ticksPerRev: Int, wheelDiameter: Double, gearRatio: Double) : this(
        ticksPerRev,
        wheelDiameter,
        gearRatio,
        0.0
    )

    constructor(ticksPerRev: Int, wheelDiameter: Double) : this(
        ticksPerRev,
        wheelDiameter,
        1.0,
        0.0
    )

    constructor(ticksPerInch: Double) : this(1, 1.0, 1.0, ticksPerInch)

    init {
        if (ticksPerInch == 0.0) ticksPerInch =
            (ticksPerRev * gearRatio) / (wheelDiameter * Math.PI)
    }

    companion object {
        @JvmStatic
        fun ticksPerIn(ticksPerInch: Double): EncoderSpecs {
            return EncoderSpecs(ticksPerInch)
        }
    }
}