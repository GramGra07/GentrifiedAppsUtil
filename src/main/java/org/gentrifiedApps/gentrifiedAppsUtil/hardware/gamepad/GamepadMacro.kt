package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe

class GamepadMacro(private val macro: List<ButtonPress>, private val func: Runnable) {
    private val macroButtons = macro.map { it.button } // Store macro sequence
    private var progress = 0 // Tracks position in macro sequence

    internal var nextExpectedButton: Button? = null
    internal var lastButton: Button? = null

    fun updateNext(){
        nextExpectedButton = if (progress < macro.size) {
            macro[progress].button
        } else {
            null
        }
    }

    fun update(gamepad: GamepadPlus){
        // should make a list to track each individual press
        // needs to sort through the list of buttons and check if the current button is the next in the sequence
        // if it is, increment progress
        // if it isn't, reset progress
        // if progress is equal to the length of the macro, run the function and reset progress
        updateNext()
        if (nextExpectedButton != null){
            if (gamepad.buttonJustPressed(nextExpectedButton!!)){
                progress++
                lastButton = nextExpectedButton
                updateNext()
            }else if (gamepad.buttonJustPressed(nextExpectedButton!!).not() && lastButton != null){
                if (gamepad.buttonJustPressed(lastButton!!).not()){
                    progress = 0
                    lastButton = null
                }
            }
        }

        if (progress == macroButtons.size) {
            func.run()
            Scribe.instance.setSet("Macro").logDebug("Macro completed: $macro")
            progress = 0
        }
    }
    internal fun updateWithButton(buttonJustPressed: Button){
        // should make a list to track each individual press
        // needs to sort through the list of buttons and check if the current button is the next in the sequence
        // if it is, increment progress
        // if it isn't, reset progress
        // if progress is equal to the length of the macro, run the function and reset progress
        updateNext()
        if (nextExpectedButton != null){
            if (buttonJustPressed == nextExpectedButton){
                progress++
                lastButton = buttonJustPressed
                updateNext()
            }else if (buttonJustPressed!= lastButton){
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
