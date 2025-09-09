package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Distance
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.DistanceUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DistanceTest {

    @Test
    fun testConvertToSameUnit() {
        val distance = Distance(100.0, DistanceUnit.CM)
        val converted = distance.convertTo(DistanceUnit.CM)
        assertEquals(100.0, converted.value)
        assertEquals(DistanceUnit.CM, converted.distanceUnit)
    }

    @Test
    fun testConvertToDifferentUnit() {
        val distance = Distance(1.0, DistanceUnit.M)
        val converted = distance.convertTo(DistanceUnit.CM)
        assertEquals(100.0, converted.value, 0.001)
        assertEquals(DistanceUnit.CM, converted.distanceUnit)
    }

    @Test
    fun testConvertToMM() {
        val distance = Distance(1.0, DistanceUnit.INCH)
        val converted = distance.convertToMM()
        assertEquals(25.4, converted.value, 0.001)
        assertEquals(DistanceUnit.MM, converted.distanceUnit)
    }

    @Test
    fun testConvertToFeet() {
        val distance = Distance(1.0, DistanceUnit.YARD)
        val converted = distance.convertTo(DistanceUnit.FEET)
        assertEquals(3.0, converted.value, 0.001)
        assertEquals(DistanceUnit.FEET, converted.distanceUnit)
    }

    @Test
    fun testConvert() {
        val distance = Distance(1.0)
        val converted = distance.convertTo(DistanceUnit.M)
        assertEquals(0.0254, converted.value, 0.001)
        assertEquals(DistanceUnit.M, converted.distanceUnit)
    }
}