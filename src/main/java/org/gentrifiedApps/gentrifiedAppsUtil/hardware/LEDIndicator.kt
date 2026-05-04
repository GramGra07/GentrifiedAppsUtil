package org.gentrifiedApps.gentrifiedAppsUtil.hardware

import com.qualcomm.robotcore.hardware.DigitalChannel
import com.qualcomm.robotcore.hardware.HardwareMap

class LEDIndicator @JvmOverloads constructor(
    hw: HardwareMap,
    number: Int,
    name1Override: String? = null,
    name2Override: String? = null
) {
    private val green: DigitalChannel
    private val red: DigitalChannel

    init {
        // Throw if exactly one name is provided (i.e. one is null/empty and the other is not).
        val n1Empty = name1Override.isNullOrEmpty()
        val n2Empty = name2Override.isNullOrEmpty()
        if (n1Empty xor n2Empty)
            throw IllegalArgumentException("LEDIndicator names must both be overridden or both omitted")
        if (!n1Empty && !n2Empty) {
            green = initDigiChan(hw, name1Override!!)
            red = initDigiChan(hw, name2Override!!)
        } else {
            green = initDigiChan(hw, "green$number")
            red = initDigiChan(hw, "red$number")
        }
    }

    private fun initDigiChan(hw: HardwareMap, name: String): DigitalChannel {
        val t = hw.get(DigitalChannel::class.java, name)
        t.mode = DigitalChannel.Mode.OUTPUT
        return t
    }

    fun turnGreen() {
        green.state = true
        red.state = false
    }

    fun turnRed() {
        green.state = false
        red.state = true
    }
}