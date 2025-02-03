package org.gentrifiedApps.gentrifiedAppsUtil.autoConfigar

import org.gentrifiedApps.gentrifiedAppsUtil.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.gamepad.GamepadPlus

class ButtonConfig(
    val button: Button,
    var value: MutableNum<*> = MutableInt(0),
) {
    lateinit var gamepad: GamepadPlus

    fun updateValue(gain: Int) {
        if (gamepad.buttonJustPressed(button)) {
            when (value) {
                is MutableInt -> (value as MutableInt).value += gain
                is MutableDouble -> (value as MutableDouble).value += gain
            }
        } else if (gamepad.buttonJustPressed(button)) {
            when (value) {
                is MutableInt -> (value as MutableInt).value -= gain
                is MutableDouble -> (value as MutableDouble).value -= gain
            }
        }
    }
}