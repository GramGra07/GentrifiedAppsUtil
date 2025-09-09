package org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test


class TrapezoidalMotionProfileTest {

    @Test
    fun testGetTarget() {
        val motionProfile = TrapezoidalMotionProfile(maxVel = 2.0, maxAccel = 1.0)
        motionProfile.generateProfile(distance = 10.0)
        motionProfile.start()

        Thread.sleep(100) // Simulate 100ms
        val (velocity, acceleration) = motionProfile.getTarget()

        // Verify velocity and acceleration at a specific time
        assert(velocity >= 0.0)
        assert(acceleration in -1.0..1.0)
    }

    @Test
    fun testReset() {
        val motionProfile = TrapezoidalMotionProfile(maxVel = 2.0, maxAccel = 1.0)
        motionProfile.generateProfile(distance = 10.0)
        motionProfile.reset()

        // Verify reset clears profile and times
        assertEquals(0.0, motionProfile.getVelocity(), 0.01)
    }

    @Test
    fun testGenerateProfileWithSmallDistance() {
        val motionProfile = TrapezoidalMotionProfile(maxVel = 2.0, maxAccel = 1.0)
        assertThrows<IllegalArgumentException> { motionProfile.generateProfile(distance = 1.0) }
    }

    @Test
    fun testGetVelocity() {
        val motionProfile = TrapezoidalMotionProfile(maxVel = 2.0, maxAccel = 1.0)
        motionProfile.generateProfile(distance = 10.0)
        motionProfile.start()

        Thread.sleep(100) // Simulate 100ms
        val velocity = motionProfile.getVelocity()

        // Verify velocity is within expected range
        assert(velocity in 0.0..2.0)
    }
}