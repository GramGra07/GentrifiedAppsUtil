package org.gentrifiedApps.gentrifiedAppsUtil

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.AngleUnit
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
    private val calc = ProjectileMotionCalculator()

    @Test
    fun calculateTimeOfFlight_valid() {
        val launchHeight = 0.0
        val angle = Angle(60.0, AngleUnit.DEGREES)
        val v0 = 50.0
        val g = calc.g
        val dy = calc.targetHeight - launchHeight
        val vy0 = v0 * sin(angle.toRadians())
        val disc = vy0 * vy0 - 2.0 * g * dy
        val expected = (vy0 + sqrt(disc)) / g
        val actual = calc.calculateTimeOfFlight(v0, angle, launchHeight)
        assertEquals(expected, actual, epsilon)
    }

    @Test
    fun calculateRange_valid() {
        val launchHeight = 0.0
        val angle = Angle(60.0, AngleUnit.DEGREES)
        val v0 = 50.0
        val t = calc.calculateTimeOfFlight(v0, angle, launchHeight)
        val expected = v0 * cos(angle.toRadians()) * t
        val actual = calc.calculateRange(v0, angle, launchHeight)
        assertEquals(expected, actual, 1e-5)
    }

    @Test
    fun calculateRequiredLaunchAngles_returnsOriginalAngle() {
        val launchHeight = 0.0
        val originalAngle = Angle(50.0, AngleUnit.DEGREES)
        val v0 = 60.0
        // Produce a consistent range using the class itself
        val producedRange = calc.calculateRange(v0, originalAngle, launchHeight)

        val angles = calc.calculateRequiredLaunchAngles(v0, producedRange)
        assertTrue(angles.isNotEmpty())
        println(angles)
        assertTrue(
            abs(angles[1].toDegrees() - originalAngle.toDegrees()) < 1e-4,
            "Expected one of returned angles to match original angle."
        )
    }

    @Test
    fun calculateRequiredInitialVelocity_invertsRangeComputation() {
        val launchHeight = 0.0
        val angle = Angle(40.0, AngleUnit.DEGREES)
        val v0 = 55.0

        val producedRange = calc.calculateRange(v0, angle, launchHeight)

        val solvedV0 = calc.calculateRequiredInitialVelocity(angle, producedRange)
        assertEquals(v0, solvedV0, 1e-4)
    }

    @Test
    fun willLand_trueForConsistentParameters() {
        val launchHeight = 0.0
        val angle = Angle(55.0)
        val v0 = 58.0
        val producedRange = calc.calculateRange(v0, angle, launchHeight)

        assertTrue(calc.willLand(v0, angle, launchHeight, producedRange))
    }

    @Test
    fun willLand_falseWhenRangeOff() {
        val launchHeight = 0.0
        val angle = Angle(55.0)
        val v0 = 58.0
        val producedRange = calc.calculateRange(v0, angle, launchHeight)

        val mismatchedRange = producedRange + 5.0
        assertFalse(calc.willLand(v0, angle, launchHeight, mismatchedRange))
    }

    @Test
    fun calculateTimeOfFlight_unreachableTarget_throws() {
        val launchHeight = 0.0
        val angle = Angle(30.0)
        val v0 = 10.0 // Too small to reach dy=38 with given g
        assertFailsWith<IllegalArgumentException> {
            calc.calculateTimeOfFlight(v0, angle, launchHeight)
        }
    }

    @Test
    fun calculateRequiredLaunchAngles_noSolution_throws() {
        val v0 = 20.0
        val impossibleRange = 5.0 // Likely too short given dy
        assertFailsWith<IllegalArgumentException> {
            calc.calculateRequiredLaunchAngles(v0, impossibleRange)
        }
    }

    @Test
    fun calculateRequiredInitialVelocity_noSolution_throws() {
        val angle = Angle(5.0)
        // Choose R so that R * tanθ <= dy making denominator <= 0
        val range = 10.0
        assertFailsWith<IllegalArgumentException> {
            calc.calculateRequiredInitialVelocity(angle, range)
        }
    }
}
