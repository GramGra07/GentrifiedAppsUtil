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
        val macro = listOf(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = listOf(Button.CIRCLE)
        val buttons2 = listOf(Button.CROSS)

        gamepadMacro.update(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons2)
        assertTrue(functionCalled)
    }

    @Test
    fun testGamepadMacroWithPartialMatch() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val macro = listOf(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = listOf(Button.CIRCLE)
        val buttons2 = listOf(Button.TRIANGLE)

        gamepadMacro.update(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons2)
        assertFalse(functionCalled)
    }

    @Test
    fun testGamepadMacroWithRepeatedButtons4() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val macro = listOf(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = listOf(Button.CIRCLE)
        val buttons2 = listOf(Button.CIRCLE, Button.CROSS)

        gamepadMacro.update(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons2)
        assertTrue(functionCalled)
    }

    @Test
    fun testGamepadMacroWithEmptyButtons() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val macro = listOf(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = emptyList<Button>()
        val buttons2 = listOf(Button.CROSS)

        gamepadMacro.update(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons2)
        assertFalse(functionCalled)
    }

    @Test
    fun testGamepadMacroWithMultipleUpdates() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val macro = listOf(buttonPress1, buttonPress2)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = listOf(Button.CIRCLE)
        val buttons2 = listOf(Button.CROSS)
        val buttons3 = listOf(Button.CIRCLE)
        val buttons4 = listOf(Button.CROSS)

        gamepadMacro.update(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons2)
        assertTrue(functionCalled)

        functionCalled = false
        gamepadMacro.update(buttons3)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons4)
        assertTrue(functionCalled)
    }
    @Test
    fun testGamepadMacroWithALot2() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val buttonPress3 = ButtonPress(Button.SQUARE)
        val buttonPress4 = ButtonPress(Button.TRIANGLE)
        val macro = listOf(buttonPress1, buttonPress2, buttonPress3, buttonPress4)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = listOf(Button.CIRCLE)
        val buttons2 = listOf(Button.CROSS)
        val buttons3 = listOf(Button.SQUARE)
        val buttons4 = listOf(Button.TRIANGLE)

        gamepadMacro.update(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons2)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons3)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons4)
        assertTrue(functionCalled)
    }

    @Test
    fun testGamepadMacroWithALot3() {
        val buttonPress1 = ButtonPress(Button.CIRCLE)
        val buttonPress2 = ButtonPress(Button.CROSS)
        val buttonPress3 = ButtonPress(Button.SQUARE)
        val buttonPress4 = ButtonPress(Button.TRIANGLE)
        val macro = listOf(buttonPress1, buttonPress2, buttonPress3, buttonPress4)
        var functionCalled = false
        val func = Runnable { functionCalled = true }

        val gamepadMacro = GamepadMacro(macro, func)

        val buttons1 = listOf(Button.CIRCLE)
        val buttons2 = listOf(Button.CROSS)
        val buttons3 = listOf(Button.SQUARE)
        val buttons4 = listOf(Button.TRIANGLE)

        gamepadMacro.update(buttons1)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons2)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons3)
        assertFalse(functionCalled)

        gamepadMacro.update(buttons4)
        assertTrue(functionCalled)
    }

    @Test
    fun testGamepadMacroFailsWithOutOfOrderInputs() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS), ButtonPress(Button.SQUARE))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.update(listOf(Button.SQUARE)) // Wrong order
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CIRCLE)) // Correct first press
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.SQUARE)) // Skipped Button.CROSS
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CROSS)) // Pressed CROSS after SQUARE
        assertFalse(functionCalled)
    }

    @Test
    fun testGamepadMacroResetsOnWrongButtonPress() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS), ButtonPress(Button.SQUARE))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.update(listOf(Button.CIRCLE)) // Correct first press
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.TRIANGLE)) // Wrong button
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CROSS)) // Cross comes after Circle, but sequence is broken
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CIRCLE)) // Restart sequence
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CROSS)) // Correct second button
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.SQUARE)) // Correct third button
        assertTrue(functionCalled) // Should trigger now
    }

    @Test
    fun testGamepadMacroWithRepeatedButtons2() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.update(listOf(Button.CIRCLE))
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CIRCLE)) // Pressed again
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CROSS)) // Final press
        assertTrue(functionCalled) // Function should trigger now
    }

    @Test
    fun testGamepadMacroWithPartialMacro() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS), ButtonPress(Button.SQUARE))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.update(listOf(Button.CIRCLE))
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CROSS))
        assertFalse(functionCalled)

        // Never pressed Button.SQUARE
        assertFalse(functionCalled)
    }

    @Test
    fun testGamepadMacroTriggersMultipleTimes() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS))
        var functionCallCount = 0
        val func = Runnable { functionCallCount++ }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.update(listOf(Button.CIRCLE))
        assertFalse(functionCallCount > 0)

        gamepadMacro.update(listOf(Button.CROSS))
        assertTrue(functionCallCount == 1) // First trigger

        gamepadMacro.update(listOf(Button.CIRCLE)) // Start again
        assertTrue(functionCallCount == 1)

        gamepadMacro.update(listOf(Button.CROSS))
        assertTrue(functionCallCount == 2) // Should trigger again
    }

    @Test
    fun testGamepadMacroIgnoresDuplicatePresses() {
        val macro = listOf(ButtonPress(Button.CIRCLE), ButtonPress(Button.CROSS))
        var functionCalled = false
        val func = Runnable { functionCalled = true }
        val gamepadMacro = GamepadMacro(macro, func)

        gamepadMacro.update(listOf(Button.CIRCLE))
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CIRCLE)) // Pressing CIRCLE multiple times should not progress
        assertFalse(functionCalled)

        gamepadMacro.update(listOf(Button.CROSS)) // Press CROSS now
        assertTrue(functionCalled) // Should only trigger now
    }
}