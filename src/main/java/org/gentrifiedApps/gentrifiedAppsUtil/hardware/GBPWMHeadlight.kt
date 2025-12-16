package org.gentrifiedApps.gentrifiedAppsUtil.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.gentrifiedApps.gentrifiedAppsUtil.classes.vision.Color

class GBPWMHeadlight @JvmOverloads constructor(
    val name: String,
    val hwMap: HardwareMap,
    val off: Double = 0.0
) {
    init {
        require(off < 1 && off > 0)
    }

    val PWM: Servo = hwMap.get(Servo::class.java, name)
    fun setRaw(p: Double) {
        PWM.position = p
    }

    fun setColor(color: Color) {
        val p: Double = when (color) {
            Color.BLUE -> 0.611 + off
            Color.RED -> 0.277 + off
            Color.YELLOW -> 0.388 + off
            Color.GREEN -> 0.5 + off
            Color.PURPLE -> 0.722 + off
            Color.WHITE -> 1.0 + off
            Color.NONE, Color.BLACK -> 0.0 + off
            Color.ORANGE -> 0.333 + off
        }
        setRaw(p)
    }
}