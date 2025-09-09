package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.firstinspires.ftc.robotcore.internal.system.Assert.assertFalse
import org.firstinspires.ftc.robotcore.internal.system.Assert.assertTrue
import org.gentrifiedApps.gentrifiedAppsUtil.classes.callbacks.Tripwire
import kotlin.test.Test

class TripwireTest {

    @Test
    fun testIsTripped() {
        val tripwire = Tripwire { true }
        assertTrue(tripwire.isTripped())

        val tripwireFalse = Tripwire { false }
        assertFalse(tripwireFalse.isTripped())
    }

    @Test
    fun testNotIsTripped() {
        val tripwire = Tripwire { true }
        assertFalse(tripwire.notIsTripped())

        val tripwireFalse = Tripwire { false }
        assertTrue(tripwireFalse.notIsTripped())
    }
}