package org.gentrifiedApps.gentrifiedAppsUtil.archive.gamepadMacroTest
//
//import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.Button
//import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.GamepadMacro
//import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.MacroBuilder
//import org.junit.Assert.assertFalse
//import org.junit.Assert.assertNotNull
//import org.junit.Assert.assertTrue
//import org.junit.Test
//
//class GamepadMacroTest {
//    @Test
//    fun testGamepadMacroInitialization() {
//        val macro = MacroBuilder().buttonPress(Button.CIRCLE).build()
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        assertNotNull(gamepadMacro)
//    }
//
//    @Test
//    fun testGamepadMacro() {
//        val button1 = Button.CIRCLE
//        val button2 = Button.CROSS
//        val macro = listOf(button1, button2)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(button1)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(button2)
//        assertTrue(functionCalled)
//    }
//
//    @Test
//    fun testGamepadMacroWithPartialMatch() {
//        val button1 = Button.CIRCLE
//        val button2 = Button.CROSS
//        val macro = listOf(button1, button2)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(button1)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.TRIANGLE)
//        assertFalse(functionCalled)
//    }
//
//    @Test
//    fun testGamepadMacroWithRepeatedButtons4() {
//        val button1 = Button.CIRCLE
//        val button2 = Button.CROSS
//        val macro = listOf(button1, button2)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(button1)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE)
//        assertFalse(functionCalled)
//        gamepadMacro.updateWithButton(Button.CROSS)
//        assertTrue(functionCalled)
//    }
//
//    @Test
//    fun testGamepadMacroWithEmptyButtons() {
//        val button1 = Button.CIRCLE
//        val button2 = Button.CROSS
//        val macro = listOf(button1, button2)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(button2)
//        assertFalse(functionCalled)
//    }
//
//    @Test
//    fun testGamepadMacroWithMultipleUpdates() {
//        val button1 = Button.CIRCLE
//        val button2 = Button.CROSS
//        val macro = listOf(button1, button2)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(button1)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(button2)
//        assertTrue(functionCalled)
//
//        functionCalled = false
//        gamepadMacro.updateWithButton(button1)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(button2)
//        assertTrue(functionCalled)
//    }
//
//    @Test
//    fun testGamepadMacroWithALot2() {
//        val button1 = Button.CIRCLE
//        val button2 = Button.CROSS
//        val button3 = Button.SQUARE
//        val button4 = Button.TRIANGLE
//        val macro = listOf(button1, button2, button3, button4)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(button1)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(button2)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(button3)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(button4)
//        assertTrue(functionCalled)
//    }
//
//    @Test
//    fun testGamepadMacroFailsWithOutOfOrderInputs() {
//        val macro = listOf(Button.CIRCLE, Button.CROSS, Button.SQUARE)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(Button.SQUARE) // Wrong order
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE) // Correct first press
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.SQUARE) // Skipped Button.CROSS
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CROSS) // Pressed CROSS after SQUARE
//        assertFalse(functionCalled)
//    }
//
//    @Test
//    fun testGamepadMacroResetsOnWrongButtonPress() {
//        val macro = listOf(Button.CIRCLE, Button.CROSS, Button.SQUARE)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE) // Correct first press
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.TRIANGLE) // Wrong button
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CROSS) // Cross comes after Circle, but sequence is broken
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE) // Restart sequence
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CROSS) // Correct second button
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.SQUARE) // Correct third button
//        assertTrue(functionCalled) // Should trigger now
//    }
//
//    @Test
//    fun testGamepadMacroWithRepeatedButtons2() {
//        val macro = listOf(Button.CIRCLE, Button.CIRCLE, Button.CROSS)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE) // Pressed again
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CROSS) // Final press
//        assertTrue(functionCalled) // Function should trigger now
//    }
//
//    @Test
//    fun testGamepadMacroTriggersMultipleTimes() {
//        val macro = listOf(Button.CIRCLE, Button.CROSS)
//        var functionCallCount = 0
//        val func = Runnable { functionCallCount++ }
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE)
//        assertFalse(functionCallCount > 0)
//
//        gamepadMacro.updateWithButton(Button.CROSS)
//        assertTrue(functionCallCount == 1) // First trigger
//
//        gamepadMacro.updateWithButton(Button.CIRCLE) // Start again
//        assertTrue(functionCallCount == 1)
//
//        gamepadMacro.updateWithButton(Button.CROSS)
//        assertTrue(functionCallCount == 2) // Should trigger again
//    }
//
//    @Test
//    fun testMacroCompletion() {
//        val macro = listOf(Button.CIRCLE, Button.CROSS)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CROSS)
//        assertTrue(functionCalled) // Function should trigger
//    }
//
//    @Test
//    fun testResetOnWrongButton() {
//        val macro = listOf(Button.CIRCLE, Button.CROSS)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.TRIANGLE) // Wrong button
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CROSS) // Correct button, but sequence is broken
//        assertFalse(functionCalled)
//    }
//
//    @Test
//    fun testPartialProgress() {
//        val macro = listOf(Button.CIRCLE, Button.CROSS, Button.SQUARE)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CROSS)
//        assertFalse(functionCalled)
//
//        // Function should not trigger until the full sequence is completed
//        gamepadMacro.updateWithButton(Button.SQUARE)
//        assertTrue(functionCalled)
//    }
//
//    @Test
//    fun testRepeatedButtons() {
//        val macro = listOf(Button.CIRCLE, Button.CIRCLE, Button.CROSS)
//        var functionCalled = false
//        val func = Runnable { functionCalled = true }
//        val gamepadMacro = GamepadMacro(macro, func)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE)
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CIRCLE) // Repeated button
//        assertFalse(functionCalled)
//
//        gamepadMacro.updateWithButton(Button.CROSS)
//        assertTrue(functionCalled) // Function should trigger
//    }
//}