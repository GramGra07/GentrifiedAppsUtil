package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad

class MacroBuilder {
    private val macro = mutableListOf<ButtonPress>()
    fun buttonPress(button: Button): MacroBuilder {
        macro.add(ButtonPress(button))
        return this
    }

    fun build(): List<ButtonPress> {
        return macro
    }
}

data class ButtonPress(val button: Button)