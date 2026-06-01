package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.DefaultAsserter.assertEquals

class WeightedSumTests {
    @Test
    fun testBasicSum() {
        val weights = arrayOf(0.5, 0.9)
        val wsum = WeightedSum(weights)
        val sum = wsum.of(arrayOf(40.0, 100.0))
        assertEquals("Weighted sum should equal", 110.0, sum)
        assertThrows<IllegalArgumentException> { wsum.of(arrayOf(40.0, 100.0, 10000.0)) }
    }
}