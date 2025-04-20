package test.kotlin

import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.clip
import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.round
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.Point
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue

class MathFunctionTest {

    @Test
    fun testAverageOf() {
        val values = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0)
        val result = MathFunctions.Companion.averageOf(values)
        assertEquals(3.0, result, "Average of [1.0, 2.0, 3.0, 4.0, 5.0] should be 3.0")
    }

    @Test
    fun testGetQuadrant() {
        val point = Point(1.0, -1.0)
        val result = MathFunctions.Companion.getQuadrant(point)
        assertEquals(4, result, "Point(1.0, -1.0) should be in quadrant 4")
    }

    @Test
    fun testThreeFourths() {
        val result = MathFunctions.Companion.threeFourths(8)
        assertEquals(6, result, "Three-fourths of 8 should be 6")
    }

    @Test
    fun testNormDelta() {
        val result = MathFunctions.Companion.normDelta(190.0)
        assertEquals(10.0, result, "Norm delta of 190.0 should be 10.0")
    }

    @Test
    fun testInRange() {
        val result = MathFunctions.Companion.inRange(5.0, 1.0, 10.0)
        assertTrue(result, "5.0 should be in range [1.0, 10.0]")
    }

    @Test
    fun testInTolerance() {
        val result = MathFunctions.Companion.inTolerance(5.0, 5.0, 0.1)
        assertTrue(result, "5.0 should be within tolerance 0.1 of 5.0")
    }

    @Test
    fun testAngleTo() {
        val p1 = Point(0.0, 0.0)
        val p2 = Point(1.0, 1.0)
        val result = MathFunctions.Companion.angleTo(p1, p2)
        assertEquals(45.0, result, "Angle from Point(0.0, 0.0) to Point(1.0, 1.0) should be 45.0 degrees")
    }

    @Test
    fun testDistanceTo() {
        val p1 = Point(0.0, 0.0)
        val p2 = Point(3.0, 4.0)
        val result = MathFunctions.Companion.distanceTo(p1, p2)
        assertEquals(5.0, result, "Distance from Point(0.0, 0.0) to Point(3.0, 4.0) should be 5.0")
    }

    @Test
    fun testClip() {
        // Test case 1: Input is less than the minimum
        assert(clip(-5.0, 0.0, 10.0) == 0.0) { "Test case 1 failed" }

        // Test case 2: Input is greater than the maximum
        assert(clip(15.0, 0.0, 10.0) == 10.0) { "Test case 2 failed" }

        // Test case 3: Input is within the range
        assert(clip(5.0, 0.0, 10.0) == 5.0) { "Test case 3 failed" }

        // Test case 4: Input is equal to the minimum
        assert(clip(0.0, 0.0, 10.0) == 0.0) { "Test case 4 failed" }

        // Test case 5: Input is equal to the maximum
        assert(clip(10.0, 0.0, 10.0) == 10.0) { "Test case 5 failed" }

        println("All test cases passed!")
    }
    @Test
    fun testRound(){
        assert(round(3.14159, 2) == 3.14)
        assert(round(3.14159, 0) == 3.0)
        assertThrows<IllegalArgumentException> {round(3.14159, -1)}
    }
}