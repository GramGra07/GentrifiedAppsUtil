package org.gentrifiedApps.gentrifiedAppsUtil

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.decode.ProjectileMotionCalculator
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProjectileMotionCalculatorTest {

    private val epsilon = 1e-6

    @Test
    fun calculateTimeOfFlight_valid() {
        val launchHeight = 0.0
        val angle = Angle(60.0)
        val v0 = 50.0
        val calc = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = v0,
            range = null
        )
        // Reproduce expected time using same formula
        val g = calc.g
        val dy = calc.targetHeight - launchHeight
        val vy0 = v0 * sin(angle.toRadians())
        val disc = vy0 * vy0 - 2.0 * g * dy
        val expected = (vy0 + sqrt(disc)) / g
        val actual = calc.calculateTimeOfFlight()
        assertEquals(expected, actual, epsilon)
    }

    @Test
    fun calculateRange_valid() {
        val launchHeight = 0.0
        val angle = Angle(60.0)
        val v0 = 50.0
        val calc = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = v0,
            range = null
        )
        val t = calc.calculateTimeOfFlight()
        val expected = v0 * cos(angle.toRadians()) * t
        val actual = calc.calculateRange()
        assertEquals(expected, actual, 1e-5)
    }

    @Test
    fun calculateRequiredLaunchAngles_returnsOriginalAngle() {
        val launchHeight = 0.0
        val originalAngle = Angle(50.0)
        val v0 = 60.0
        // Produce a consistent range using the class itself
        val temp = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = originalAngle,
            initialVelocity = v0,
            range = null
        )
        val producedRange = temp.calculateRange()

        val inverse = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = null,
            initialVelocity = v0,
            range = producedRange
        )
        val angles = inverse.calculateRequiredLaunchAngles()
        assertTrue(angles.isNotEmpty())
        assertTrue(
            angles.any { abs(it.angle - originalAngle.angle) < 1e-4 },
            "Expected one of returned angles to match original angle."
        )
    }

    @Test
    fun calculateRequiredInitialVelocity_invertsRangeComputation() {
        val launchHeight = 0.0
        val angle = Angle(40.0)
        val v0 = 55.0

        val forward = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = v0,
            range = null
        )
        val producedRange = forward.calculateRange()

        val inverse = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = null,
            range = producedRange
        )
        val solvedV0 = inverse.calculateRequiredInitialVelocity()
        assertEquals(v0, solvedV0, 1e-4)
    }

    @Test
    fun willLand_trueForConsistentParameters() {
        val launchHeight = 0.0
        val angle = Angle(55.0)
        val v0 = 58.0
        val forward = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = v0,
            range = null
        )
        val producedRange = forward.calculateRange()

        val check = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = v0,
            range = producedRange
        )
        assertTrue(check.willLand())
    }

    @Test
    fun willLand_falseWhenRangeOff() {
        val launchHeight = 0.0
        val angle = Angle(55.0)
        val v0 = 58.0
        val forward = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = v0,
            range = null
        )
        val producedRange = forward.calculateRange()

        val mismatched = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = v0,
            range = producedRange + 1.0
        )
        assertFalse(mismatched.willLand())
    }

    @Test
    fun calculateTimeOfFlight_unreachableTarget_throws() {
        val launchHeight = 0.0
        val angle = Angle(30.0)
        val v0 = 10.0 // Too small to reach dy=38 with given g
        val calc = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = v0,
            range = null
        )
        assertFailsWith<IllegalArgumentException> {
            calc.calculateTimeOfFlight()
        }
    }

    @Test
    fun calculateRequiredLaunchAngles_noSolution_throws() {
        val launchHeight = 0.0
        val v0 = 20.0
        val impossibleRange = 5.0 // Too short while still needing large vertical displacement
        val calc = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = null,
            initialVelocity = v0,
            range = impossibleRange
        )
        assertFailsWith<IllegalArgumentException> {
            calc.calculateRequiredLaunchAngles()
        }
    }

    @Test
    fun calculateRequiredInitialVelocity_noSolution_throws() {
        val launchHeight = 0.0
        val angle = Angle(5.0)
        // Choose R so that R * tanθ <= dy making denominator <= 0
        val range = 10.0
        val calc = ProjectileMotionCalculator(
            launchHeight = launchHeight,
            launchAngle = angle,
            initialVelocity = null,
            range = range
        )
        assertFailsWith<IllegalArgumentException> {
            calc.calculateRequiredInitialVelocity()
        }
    }
}
