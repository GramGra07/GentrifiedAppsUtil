package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad

class MacroBuilder {
    private val macro = mutableListOf<Button>()

    fun buttonPress(button: Button): MacroBuilder {
        macro.add((button))
        return this
    }

    fun build(): List<Button> {
        return macro
    }
}
