package org.gentrifiedApps.gentrifiedAppsUtil

import org.gentrifiedApps.gentrifiedAppsUtil.biobuzz.Pollen
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.DistanceUnit
import kotlin.test.Test

class BioBuzzTests {
    @Test
    fun testBioBuzzMain() {
        assert(Pollen.diameter.convertTo(DistanceUnit.INCH).value == 3.0)
    }
}