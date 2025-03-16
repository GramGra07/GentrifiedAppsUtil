package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.servo

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

class AxonServo(hw: HardwareMap, val name: String) {
    private val encoder: AnalogInput
    private val servo: ServoPlus

    init {
        encoder = initAEncoder(hw)
        servo = ServoPlus(hw, name)
    }

    private fun initAEncoder(hw: HardwareMap): AnalogInput {
        return hw.get(AnalogInput::class.java, name + "Encoder")
    }

    fun telemetry(telemetry: Telemetry) {
        telemetry.addData("$name aEncoder", "%.1f", this.getEncoderPosition())
    }

    fun setPosition(degree: Double) {
        servo.position = (degree)
    }

    fun getEncoderPosition(): Double {
        return (encoder.voltage / 3.3) * 360
    }

    fun getEncoderPositionReversed(): Double {
        return (((encoder.voltage - 3.3) / 3.3) * 360) * -1
    }
}