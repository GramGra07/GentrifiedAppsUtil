package org.gentrifiedApps.gentrifiedAppsUtil.hardware.sensors

import com.qualcomm.robotcore.hardware.DigitalChannel
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

class BeamBreakSensor(hw: HardwareMap, internal val name: String) {
    companion object {
        @JvmStatic
        fun newInstance(hw: HardwareMap, name: String): BeamBreakSensor {
            return BeamBreakSensor(hw, name)
        }
    }

    private val beamBreak: DigitalChannel

    init {
        beamBreak = initBeamBreak(hw)
        beamBreak.state = true
    }

    private fun initBeamBreak(hw: HardwareMap): DigitalChannel {
        return hw.get(DigitalChannel::class.java, name)
    }

    fun isBroken(): Boolean = !beamBreak.state
    private fun isOpen(): Boolean = beamBreak.state
    fun telemetry(telemetry: Telemetry) {
        telemetry.addData("$name is broken", isBroken())
    }
}