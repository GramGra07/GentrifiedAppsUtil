package org.gentrifiedApps.gentrifiedAppsUtil.classes

import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap

/**
 * A class to compensate for voltage drop, allowing for consistent motor power
 * @param kf The kf value to use (and tune)
 */
class VoltageCompensator(private val kf:Double) {

    val voltageSensor: VoltageSensor = hardwareMap.voltageSensor.first()
    fun getVoltage(): Double {
        return voltageSensor.voltage
    }

    /**
     * @param controlEffort The control effort to get the voltage compensated kf
     * @return The voltage compensated power for your motor
     */
    fun getVoltageCompensatedKf(controlEffort:Double):Double{
        return (12/getVoltage())*kf*controlEffort
    }
}