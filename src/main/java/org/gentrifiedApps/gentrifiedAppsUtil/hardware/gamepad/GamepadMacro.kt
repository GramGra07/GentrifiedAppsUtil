package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad


import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

class GamepadMacro(private val macro: List<Button>, private val func: Runnable) {
    constructor(vararg macro: Button, func: Runnable) : this(macro.toList(), func)

    private val macroButtons = macro.map { it } // Store macro sequence
    private var progress = 0 // Tracks position in macro sequence

    private var nextExpectedButton: Button? = null
    private var lastButton: Button? = null


    private fun updateNext() {
        nextExpectedButton = if (progress < macro.size) {
            macro[progress]
        } else {
            null
        }
    }

    fun update(gamepad: GamepadPlus) {
        // Cache the button state to avoid redundant calls
        val justPressedButtons = gamepad.getButtonsCurrentlyPressed()
//        Scribe.instance.setSet("Macro").logDebug("progress $progress")
//        Scribe.instance.setSet("Macro").logDebug("justPressedButtons $justPressedButtons")

        if (nextExpectedButton != null) {
            if (gamepad.buttonJustPressed(nextExpectedButton!!.resolveAlias())) {
                progress++
                lastButton = nextExpectedButton
            } else if (lastButton != null && justPressedButtons.isNotEmpty() && justPressedButtons.any { it.resolveAlias() != lastButton!!.resolveAlias() }) {
                progress = 0
                lastButton = null
            }
        }

        if (progress == macroButtons.size) {
            func.run()
            Scribe.instance.setSet("Macro").logDebug("Macro completed: $macro")
            progress = 0
            lastButton = null
        }

        updateNext()
    }

    internal fun updateWithButton(buttonJustPressed: Button) {
        // should make a list to track each individual press
        // needs to sort through the list of buttons and check if the current button is the next in the sequence
        // if it is, increment progress
        // if it isn't, reset progress
        // if progress is equal to the length of the macro, run the function and reset progress
        updateNext()
        if (nextExpectedButton != null) {
            if (buttonJustPressed == nextExpectedButton) {
                progress++
                lastButton = buttonJustPressed
                updateNext()
            } else if (buttonJustPressed != lastButton) {
                progress = 0
                lastButton = null
            }
        }

        if (progress == macroButtons.size) {
            func.run()
            Scribe.instance.setSet("Macro").logDebug("Macro completed: $macro")
            progress = 0
            lastButton = null
        }
    }
}
