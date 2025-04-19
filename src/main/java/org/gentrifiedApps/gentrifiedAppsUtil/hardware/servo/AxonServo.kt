package org.gentrifiedApps.gentrifiedAppsUtil.hardware.servo

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions
import org.gentrifiedApps.gentrifiedAppsUtil.classes.analogEncoder.AnalogEncoder

enum class AxonAlgorithm {
    REGULAR,
    REVERSED;

    fun reversed(): AxonAlgorithm {
        return if (this == REGULAR) {
            REVERSED
        } else {
            REGULAR
        }
    }
}

class AxonServo(hw: HardwareMap, private val name: String) {
    private var encoder: AnalogEncoder
    private val servo: ServoPlus
    private var algorithm: AxonAlgorithm = AxonAlgorithm.REGULAR

    init {
        encoder = initAEncoder(hw)
        servo = ServoPlus(hw, name)
    }

    fun setAlgo(algorithm: AxonAlgorithm): AxonServo {
        this.algorithm = algorithm
        return this
    }

    fun checkError() {
        val errorTolerance = 20
        if (errorTolerance > getError()) {
            setAlgo(this.algorithm.reversed())
        }
    }

    private fun initAEncoder(hw: HardwareMap): AnalogEncoder {
        return AnalogEncoder.axon(hw, name + "Encoder")
    }

    fun telemetry(telemetry: Telemetry) {
        telemetry.addData("$name aEncoder", "%.1f", this.getEncoderPosition())
    }

    fun setPosition(degree: Double) {
        servo.position = (degree)
    }

    fun setPositionCheck(degree: Double) {
        servo.position = (degree)
        checkError()
    }

    fun getEncoderPosition(): Double {
        return when (algorithm) {
            AxonAlgorithm.REGULAR -> getEncoderPositionRegular()
            AxonAlgorithm.REVERSED -> getEncoderPositionReversed()
        }
    }

    fun getEncoderPositionRegular(): Double {
        return encoder.getCurrentPosition().toDouble()
    }

    fun getEncoderPositionReversed(): Double {
        return (((encoder.getVoltage() - 3.3) / 3.3) * 360) * -1
    }

    fun getError(): Double {
        return MathFunctions.getError(this.servo.position, this.getEncoderPosition().toDouble())
    }
}