package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles

import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test


class PWMMotionProfileTest {
    @Test
    fun testPWMLoops() {
        val profile = PWMMotionProfile(PWMMotionProfile.PWMType.LOOPS, 5.0)
        assertEquals("Initial power should be 0.0", 0.0, profile.getPower())

        // The first 5 updates should not change the power
        for (i in 1..5) {
            profile.update()
            assertEquals("After ${i}th update, power should be 0.0", 0.0, profile.getPower())
        }

        // On the 6th update, power should become 1.0
        profile.update()
        assertEquals("After 6th update, power should be 1.0", 1.0, profile.getPower())

        // The next 4 updates should not change the power
        for (i in 7..10) {
            profile.update()
            assertEquals("After ${i}th update, power should be 1.0", 1.0, profile.getPower())
        }

        // On the 11th update, power should become 0.0 again
        profile.update()
        assertEquals("After 11th update, power should be 0.0", 0.0, profile.getPower())
    }

    @Test
    fun testPWMSeconds() {
        // Using a mock timer would be better, but for this test, Thread.sleep will do.
        val profile = PWMMotionProfile(PWMMotionProfile.PWMType.SECONDS, 0.1)
        assertEquals("Initial power should be 0.0", 0.0, profile.getPower())

        profile.update()
        assertEquals("Power should be 0.0 before period has passed", 0.0, profile.getPower())

        Thread.sleep(110) // Wait for more than the period (0.1s)

        profile.update()
        assertEquals("Power should be 1.0 after period has passed", 1.0, profile.getPower())

        profile.update()
        assertEquals("Power should remain 1.0", 1.0, profile.getPower())

        Thread.sleep(110)

        profile.update()
        assertEquals("Power should be 0.0 after another period has passed", 0.0, profile.getPower())
    }
}