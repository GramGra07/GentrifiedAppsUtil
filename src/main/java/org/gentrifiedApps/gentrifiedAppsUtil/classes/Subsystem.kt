package org.gentrifiedApps.gentrifiedAppsUtil.classes

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

abstract class Subsystem(private val hwMap: HardwareMap, auto: Boolean = false) {
    constructor(hwMap: HardwareMap) : this(hwMap, false)

    abstract fun init()
    abstract fun update()
    abstract fun telemetry(telemetry: Telemetry)
}

//class ArmSub(hwMap: HardwareMap) : Subsystem(hwMap) {
//    override fun init() {
//
//    }
//
//    override fun update() {
//
//    }
//
//    override fun telemetry(telemetry: Telemetry) {
//
//    }
//}