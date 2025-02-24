package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad

class GamepadMacro(private val macro: List<ButtonPress>, private val func: Runnable) {
    private val macroButtons = macro.map { it.button } // Store macro sequence
    private var progress = 0 // Tracks position in macro sequence

    fun update(buttons: List<Button>) {
        if (buttons.contains(macroButtons[progress])) {
            progress++ // Move to the next expected button
            if (progress == macroButtons.size) {
                func.run()
                progress = 0 // Reset after activation
            }
        } else if (!buttons.contains(macroButtons.getOrNull(progress - 1))) {
            progress = 0 // Reset if sequence is broken
        }
    }
}
