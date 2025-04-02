package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.voltage

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.VoltageSensor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.classes.VoltageCompensator

/**
 * A class to track voltage drop
 * @param hardwareMap The hardware map to use
 * @param usesVoltageCompensator Whether or not to use the voltage compensator
 * @param kf The kf value to use (and tune)
 */
class VoltageTracker(
    hardwareMap: HardwareMap,
    private val usesVoltageCompensator: Boolean,
    kf: Double
) {
    constructor(hardwareMap: HardwareMap) : this(hardwareMap, false, 0.0)
    constructor(hardwareMap: HardwareMap, kf: Double) : this(hardwareMap, true, kf)

    private var voltageCompensator : VoltageCompensator? = if (usesVoltageCompensator){
        VoltageCompensator(kf)
    }else{
        null
    }


    private var voltageSensor: VoltageSensor = if (usesVoltageCompensator) {
        voltageCompensator!!.voltageSensor
    } else {
        hardwareMap.voltageSensor.first()
    }

    var voltageIndex: Int = 0

    var cached: Boolean = false
    fun clearCache() {
        cached = false
    }

    var nominalVoltage: Double = 12.5

    var cacheInvalidateSeconds: Double = 0.5
    private val timer: ElapsedTime = ElapsedTime()

    private var initialVoltage = 0.0
    private var currentVoltage = 0.0
    private var voltageDrop = 0.0
    private var lowestVoltage = Double.POSITIVE_INFINITY

    init {
        initialVoltage = voltageSensor.voltage
        currentVoltage = initialVoltage
        timer.reset()
    }

    fun update() {
        currentVoltage = voltageSensor.voltage
        voltageDrop = initialVoltage - currentVoltage

        if (timer.seconds() > cacheInvalidateSeconds && cacheInvalidateSeconds >= 0) {
            clearCache()
        }

        if (!cached) {
            cached = true

        }
        if (currentVoltage < lowestVoltage) {
            lowestVoltage = currentVoltage
            if (lowestVoltage < 9.0) {
                Scribe.instance.setSet("V").logWarning("Dropped Voltage to: $lowestVoltage")
            }
        }
    }
    fun getVoltagePercent(): Double {
        return currentVoltage/nominalVoltage
    }

    /**
     * Returns the current voltage and voltage drop in telemetry
     * @param telemetry The telemetry to use
     */
    fun telemetry(telemetry: Telemetry) {
        update()
        telemetry.addLine("Voltage: ${currentVoltage.format(3)}, ${(getVoltagePercent() * 100).format(2)}%")
        telemetry.addLine("Voltage Drop: ${voltageDrop.format(2)}")
        telemetry.addLine("Lowest Voltage: ${lowestVoltage.format(2)}")
    }

    fun calculateVoltageCompensatedKf(controlEffort: Double): Double {
        require(usesVoltageCompensator)
        return voltageCompensator!!.getVoltageCompensatedKf(controlEffort)
    }
}

private fun Double.format(i: Int): String {
    return String.format("%.${i}f", this)
}