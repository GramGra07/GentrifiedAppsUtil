package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.junit.jupiter.api.Test

class VectorTest {
    @Test
    fun testNormalVec() {
        val v = Vector(0.0, 1.0, 2.0)
        val u = Vector(2.0, 3.0, 1.0)
        val sum = u + v
        assert(sum.a == 2.0)
        assert(sum.b == 4.0)
        assert(sum.c == 3.0)
    }

    @Test
    fun test2DVec() {
        val v = Vector2d(2.0, 5.0)
        val u = Vector2d(1.0, 2.0)
        val sum = v + u
        val minus = v - u
        assert(sum.a == 3.0)
        assert(sum.b == 7.0)
    }
}