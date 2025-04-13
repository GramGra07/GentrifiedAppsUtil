package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

class GamepadMacro(private val macro: List<ButtonPress>, private val func: Runnable) {
    private val macroButtons = macro.map { it.button } // Store macro sequence
    private var progress = 0 // Tracks position in macro sequence

    fun update(buttons: List<Button>) {
        // should make a list to track each individual press
        // needs to sort through the list of buttons and check if the current button is the next in the sequence
        // if it is, increment progress
        // if it isn't, reset progress
        // if progress is equal to the length of the macro, run the function and reset progress
        if (buttons.contains(macroButtons[progress])) {
            progress++
        }
        if (progress == macroButtons.size) {
            func.run()
            Scribe.instance.setSet("Macro").logDebug("Macro completed: $macro")
            progress = 0
        }
    }
}
