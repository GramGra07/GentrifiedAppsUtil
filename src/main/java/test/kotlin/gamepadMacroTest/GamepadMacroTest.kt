package test.kotlin.gamepadMacroTest

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.*
import org.junit.Test

class GamepadMacroTest {
    @Test
    fun testGamepadMacroInitialization() {
        val macro = MacroBuilder().buttonPress(Button.CIRCLE).build()
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        assertNotNull(gamepadMacro)
    }

    @Test
    fun testGamepadMacro() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val macro = listOf<ButtonPress>(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = (Button.CIRCLE)
        val buttons2 = (Button.CROSS)

        gamepadMacro.updateWithButton(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons2)
        assertTrue(functionCalled)
    }

    @Test
    fun testGamepadMacroWithPartialMatch() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val macro = listOf<ButtonPress>(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = (Button.CIRCLE)
        val buttons2 = (Button.TRIANGLE)

        gamepadMacro.updateWithButton(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons2)
        assertFalse(functionCalled)
    }

    @Test
    fun testGamepadMacroWithRepeatedButtons4() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val macro = listOf<ButtonPress>(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = (Button.CIRCLE)

        gamepadMacro.updateWithButton(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(Button.CIRCLE)
        assertFalse(functionCalled)
        gamepadMacro.updateWithButton(Button.CROSS)
        assertTrue(functionCalled)
    }

    @Test
    fun testGamepadMacroWithEmptyButtons() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val macro = listOf<ButtonPress>(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons2 = (Button.CROSS)

        gamepadMacro.updateWithButton(buttons2)
        assertFalse(functionCalled)
    }

    @Test
    fun testGamepadMacroWithMultipleUpdates() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val macro = listOf<ButtonPress>(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = (Button.CIRCLE)
        val buttons2 = (Button.CROSS)
        val buttons3 = (Button.CIRCLE)
        val buttons4 = (Button.CROSS)

        gamepadMacro.updateWithButton(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons2)
        assertTrue(functionCalled)

        functionCalled = false
        gamepadMacro.updateWithButton(buttons3)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons4)
        assertTrue(functionCalled)
    }
    @Test
    fun testGamepadMacroWithALot2() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val buttonPress3 = ButtonPress(Button.SQUARE)
        val buttonPress4 = ButtonPress(Button.TRIANGLE)
        val macro = listOf<ButtonPress>(buttonPress1, buttonPress2, buttonPress3, buttonPress4)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = (Button.CIRCLE)
        val buttons2 = (Button.CROSS)
        val buttons3 = (Button.SQUARE)
        val buttons4 = (Button.TRIANGLE)

        gamepadMacro.updateWithButton(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons2)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons3)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons4)
        assertTrue(functionCalled)
    }

    @Test
    fun testGamepadMacroWithALot3() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val buttonPress3 = ButtonPress(Button.SQUARE)
        val buttonPress4 = ButtonPress(Button.TRIANGLE)
        val macro = listOf<ButtonPress>(buttonPress1, buttonPress2, buttonPress3, buttonPress4)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = (Button.CIRCLE)
        val buttons2 = (Button.CROSS)
        val buttons3 = (Button.SQUARE)
        val buttons4 = (Button.TRIANGLE)

        gamepadMacro.updateWithButton(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons2)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons3)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(buttons4)
        assertTrue(functionCalled)
    }

    @Test
    fun testGamepadMacroFailsWithOutOfOrderInputs() {
        val macro = listOf<ButtonPress>(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS), ButtonPress(Button.SQUARE))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton((Button.SQUARE)) // Wrong order
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CIRCLE)) // Correct first press
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.SQUARE)) // Skipped Button.CROSS
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CROSS)) // Pressed CROSS after SQUARE
        assertFalse(functionCalled)
    }

    @Test
    fun testGamepadMacroResetsOnWrongButtonPress() {
        val macro = listOf<ButtonPress>(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS), ButtonPress(Button.SQUARE))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton((Button.CIRCLE)) // Correct first press
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.TRIANGLE)) // Wrong button
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CROSS)) // Cross comes after Circle, but sequence is broken
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CIRCLE)) // Restart sequence
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CROSS)) // Correct second button
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.SQUARE)) // Correct third button
        assertTrue(functionCalled) // Should trigger now
    }

    @Test
    fun testGamepadMacroWithRepeatedButtons2() {
        val macro = listOf<ButtonPress>(ButtonPress(Button.CIRCLE), ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton((Button.CIRCLE))
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CIRCLE)) // Pressed again
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CROSS)) // Final press
        assertTrue(functionCalled) // Function should trigger now
    }

    @Test
    fun testGamepadMacroWithPartialMacro() {
        val macro = listOf<ButtonPress>(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS), ButtonPress(Button.SQUARE))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton((Button.CIRCLE))
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CROSS))
        assertFalse(functionCalled)

        // Never pressed Button.SQUARE
        assertFalse(functionCalled)
    }

    @Test
    fun testGamepadMacroTriggersMultipleTimes() {
        val macro = listOf<ButtonPress>(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS))
        var functionCallCount = 0
        val func = Runnable { functionCallCount++ }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton((Button.CIRCLE))
        assertFalse(functionCallCount > 0)

        gamepadMacro.updateWithButton((Button.CROSS))
        assertTrue(functionCallCount == 1) // First trigger

        gamepadMacro.updateWithButton((Button.CIRCLE)) // Start again
        assertTrue(functionCallCount == 1)

        gamepadMacro.updateWithButton((Button.CROSS))
        assertTrue(functionCallCount == 2) // Should trigger again
    }

    @Test
    fun testGamepadMacroIgnoresDuplicatePresses() {
        val macro = (listOf<ButtonPress>(ButtonPress(Button.CIRCLE),ButtonPress(Button.CROSS)))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton((Button.CIRCLE))
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CIRCLE)) // Pressing CIRCLE multiple times should not progress
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton((Button.CROSS)) // Press CROSS now
        assertTrue(functionCalled) // Should only trigger now
    }

    @Test
    fun testMacroCompletion() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton(Button.CIRCLE)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(Button.CROSS)
        assertTrue(functionCalled) // Function should trigger
    }

    @Test
    fun testResetOnWrongButton() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton(Button.CIRCLE)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(Button.TRIANGLE) // Wrong button
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(Button.CROSS) // Correct button, but sequence is broken
        assertFalse(functionCalled)
    }

    @Test
    fun testPartialProgress() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS), ButtonPress(Button.SQUARE))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton(Button.CIRCLE)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(Button.CROSS)
        assertFalse(functionCalled)

        // Function should not trigger until the full sequence is completed
        gamepadMacro.updateWithButton(Button.SQUARE)
        assertTrue(functionCalled)
    }

    @Test
    fun testRepeatedButtons() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.updateWithButton(Button.CIRCLE)
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(Button.CIRCLE) // Repeated button
        assertFalse(functionCalled)

        gamepadMacro.updateWithButton(Button.CROSS)
        assertTrue(functionCalled) // Function should trigger
    }
}