package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.voltage

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.VoltageCompensator

/**
 * A class to track voltage drop
 * @param hardwareMap The hardware map to use
 * @param usesVoltageCompensator Whether or not to use the voltage compensator
 * @param kf The kf value to use (and tune)
 */
class VoltageTracker(hardwareMap: HardwareMap, private val usesVoltageCompensator:Boolean, kf:Double) {
    constructor(hardwareMap: HardwareMap) : this(hardwareMap, false,0.0)
    private val voltageCompensator = VoltageCompensator(kf)


    private var voltageSensor :VoltageSensor = if (usesVoltageCompensator){
        voltageCompensator.voltageSensor
    } else {
        hardwareMap.voltageSensor.first()
    }
    private var initialVoltage = 0.0
    private var currentVoltage = 0.0
    private var voltageDrop = 0.0
    private var lowestVoltage = 0.0

    init{
        initialVoltage = voltageSensor.voltage
        currentVoltage = initialVoltage
    }

    fun update(){
        currentVoltage = voltageSensor.voltage
        voltageDrop = initialVoltage - currentVoltage
        if (currentVoltage < lowestVoltage) {
            lowestVoltage = currentVoltage
        }
    }

    /**
     * Returns the current voltage and voltage drop in telemetry
     * @param telemetry The telemetry to use
     */
    fun telemetry(telemetry: Telemetry){
        update()
        telemetry.addLine("Voltage: ${currentVoltage.format(3)}")
        telemetry.addLine("Voltage Drop: ${voltageDrop.format(2)}")
        telemetry.addLine("Lowest Voltage: ${lowestVoltage.format(2)}")
    }

    fun calculateVoltageCompensatedKf(controlEffort:Double):Double{
        require(usesVoltageCompensator)
        return voltageCompensator.getVoltageCompensatedKf(controlEffort)
    }
}

private fun Double.format(i: Int): String {
    return String.format("%.${i}f", this)
}