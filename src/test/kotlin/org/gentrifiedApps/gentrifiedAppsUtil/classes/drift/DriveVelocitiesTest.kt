package org.gentrifiedApps.gentrifiedAppsUtil.classes.drift

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Quadruple
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.drift.DriveVelocities
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class DriveVelocitiesTest {

    @Test
    fun testApplyDriftNormalizer() {
        val velocities = DriveVelocities(1.0, 0.8, 0.9, 1.0)
        val result = velocities.applyDriftNormalizer()

        // Expected values based on the logic in applyDriftNormalizer
        val expected = Quadruple(0.8, 1.0, 0.9, 0.8) // Adjust based on expected behavior
        assertEquals(expected, result)
    }

    @Test
    fun testDivisionOperator() {
        val velocities = DriveVelocities(2.0, 4.0, 6.0, 8.0)
        val result = velocities / 2.0

        assertEquals(DriveVelocities(1.0, 2.0, 3.0, 4.0), result)
    }

    @Test
    fun testAsPercent() {
        val velocities = DriveVelocities(1.0, 2.0, 3.0, 4.0)
        val result = velocities.asPercent()

        assertEquals(DriveVelocities(0.25, 0.5, 0.75, 1.0), result)
    }

    @Test
    fun testMinAndMax() {
        val velocities = DriveVelocities(1.0, 2.0, 3.0, 4.0)
        assertEquals(1.0, velocities.min())
        assertEquals(4.0, velocities.max())
    }

    @Test
    fun testApplyDriftNormalizerFULL() {
        val velocities = DriveVelocities(1000, 800, 900, 1000)
        val result = velocities.asPercent().applyDriftNormalizer()

        // Expected values based on the logic in applyDriftNormalizer
        val expected = Quadruple(0.8, 1.0, 0.9, 0.8) // Adjust based on expected behavior
        assertEquals(expected, result)
    }
}