package org.gentrifiedApps.gentrifiedAppsUtil.classes

import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap

/**
 * A class to compensate for voltage drop, allowing for consistent motor power
 * @param name The name of the voltage sensor, usually Expansion Hub
 * @param kf The kf value to use (and tune)
 */
class VoltageCompensator(val name:String, private val kf:Double) {
    constructor() : this("Expansion Hub 1",1.0)
    constructor(name:String) : this(name,1.0)
    constructor(kf:Double) : this("Expansion Hub 1",kf)

    private val voltageSensor: VoltageSensor = hardwareMap.get(VoltageSensor::class.java, name)
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
VoltageCompensator(name,kf)
        getVoltageCompensatedKf(controlEffort)