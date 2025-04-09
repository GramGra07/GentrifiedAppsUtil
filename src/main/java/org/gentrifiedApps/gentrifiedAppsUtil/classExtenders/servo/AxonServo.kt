package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.servo

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.analogEncoder.AnalogEncoder

class AxonServo(hw: HardwareMap, val name: String) {
    private var encoder: AnalogEncoder
    private val servo: ServoPlus

    init {
        encoder = initAEncoder(hw)
        servo = ServoPlus(hw, name)
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

    fun getEncoderPosition(): Int {
        return encoder.getCurrentPosition()
    }

    fun getEncoderPositionReversed(): Double {
        return (((encoder.getVoltage() - 3.3) / 3.3) * 360) * -1
    }
}