package test.kotlin.motion.profiles

import org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles.AccelerationMotionProfile
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.sqrt

class AccelerationMotionProfileTest {
    @Test
    fun testGetTarget() {
        val motionProfile = AccelerationMotionProfile(maxVel = 2.0, maxAccel = 1.0)
        motionProfile.generateProfile(distance = 10.0)
        motionProfile.start()

        Thread.sleep(100) // Simulate 100ms
        val (velocity, acceleration) = motionProfile.getTarget()

        // Verify velocity and acceleration at a specific time
        assert(velocity >= 0.0)
        assert(acceleration in 0.0..1.0)
    }

    @Test
    fun testReset() {
        val motionProfile = AccelerationMotionProfile(maxVel = 2.0, maxAccel = 1.0)
        motionProfile.generateProfile(distance = 10.0)
        motionProfile.regenerateProfile(10.0)

        // Verify reset clears profile and times
        assertEquals(0.0, motionProfile.getVelocity(), 0.02)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testGenerateProfileWithInvalidDistance() {
        val motionProfile = AccelerationMotionProfile(maxVel = 2.0, maxAccel = 1.0)
        motionProfile.generateProfile(distance = -5.0) // Should throw an exception
    }

    @Test
    fun testGetVelocity() {
        val motionProfile = AccelerationMotionProfile(maxVel = 2.0, maxAccel = 1.0)
        motionProfile.generateProfile(distance = 10.0)
        motionProfile.start()

        Thread.sleep(100) // Simulate 100ms
        val velocity = motionProfile.getVelocity()

        // Verify velocity is within expected range
        assert(velocity in 0.0..2.0)
    }
}