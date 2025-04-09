package test.kotlin.motion.profiles

import org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles.OnlyUpSlewRateLimiter
import org.junit.Assert.assertEquals
import org.junit.Test

class OnlyUpSlewRateLimiterTest {

    @Test
    fun testPositiveDeltaWithinLimit() {
        val rateLimiter = OnlyUpSlewRateLimiter(2.0) // Rate limit of 2 units per second
        rateLimiter.calculate(0.0) // Initialize timer

        Thread.sleep(500) // Simulate 0.5 seconds
        val result = rateLimiter.calculate(1.0) // Input within limit
        assertEquals(1.0, result, 0.01)
    }

    @Test
    fun testPositiveDeltaExceedsLimit() {
        val rateLimiter = OnlyUpSlewRateLimiter(2.0) // Rate limit of 2 units per second
        rateLimiter.calculate(0.0) // Initialize timer

        Thread.sleep(500) // Simulate 0.5 seconds
        val result = rateLimiter.calculate(3.0) // Input exceeds limit
        assertEquals(1.0, result, 0.05) // Limited to 1.0 (2 * 0.5)
    }

    @Test
    fun testNegativeDelta() {
        val rateLimiter = OnlyUpSlewRateLimiter(2.0) // Rate limit of 2 units per second
        rateLimiter.calculate(2.0) // Initialize timer with a starting value

        Thread.sleep(500) // Simulate 0.5 seconds
        val result = rateLimiter.calculate(1.0) // Negative delta
        assertEquals(1.0, result, 0.01) // Negative deltas are allowed
    }

    @Test
    fun testZeroDelta() {
        val rateLimiter = OnlyUpSlewRateLimiter(2.0) // Rate limit of 2 units per second
        rateLimiter.calculate(1.0) // Initialize timer with a starting value

        Thread.sleep(500) // Simulate 0.5 seconds
        val result = rateLimiter.calculate(1.0) // No change in input
        assertEquals(1.0, result, 0.01) // Output remains the same
    }

    @Test
    fun testMultiplePositiveDeltas() {
        val rateLimiter = OnlyUpSlewRateLimiter(2.0) // Rate limit of 2 units per second
        rateLimiter.calculate(0.0) // Initialize timer

        Thread.sleep(500) // Simulate 0.5 seconds
        var result = rateLimiter.calculate(1.0) // First input
        assertEquals(1.0, result, 0.01)

        Thread.sleep(500) // Simulate another 0.5 seconds
        result = rateLimiter.calculate(3.0) // Second input exceeds limit
        assertEquals(2.0, result, 0.03) // Limited to 2.0 (2 * 0.5 + 1.0)
    }
}