package org.gentrifiedApps.gentrifiedAppsUtil.hardware.voltage

import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion

/**
 * A class to compensate for voltage drop, allowing for consistent motor power
 * @param kf The kf value to use (and tune)
 */
class VoltageCompensator(private val kf: Double) {
    init {
        require(kf > 0.0) { "kf must be greater than 0" }
    }

    val voltageSensor: VoltageSensor = BlocksOpModeCompanion.hardwareMap.voltageSensor.first()
    fun getVoltage(): Double {
        return voltageSensor.voltage
    }

    /**
     * @param controlEffort The control effort to get the voltage compensated kf
     * @return The voltage compensated power for your motor
     */
    fun getVoltageCompensatedKf(controlEffort: Double): Double {
        return (12 / getVoltage()) * kf * controlEffort
    }
}