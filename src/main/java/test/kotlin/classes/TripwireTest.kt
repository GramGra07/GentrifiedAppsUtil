package test.kotlin.classes

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.gentrifiedApps.gentrifiedAppsUtil.classes.callbacks.Tripwire
import org.junit.Test

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