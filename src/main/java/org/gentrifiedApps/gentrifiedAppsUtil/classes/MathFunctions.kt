package org.gentrifiedApps.gentrifiedAppsUtil.classes

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot

class MathFunctions {
    companion object {
    fun averageOf(values: DoubleArray): Double {
        return values.average()
    }

    fun getQuadrant(pose: Point): Int {
        var x = pose.x.toInt()
        var y = pose.y.toInt()
        if (x == 0) {
            x = (x + 0.1).toInt() // compensates for divide by 0 error
        }
        if (y == 0) {
            y = (y + 0.1).toInt() // compensates for divide by 0 error
        }
        val xSign = x.toDouble() / abs(x) // gets sign of x
        val ySign = y.toDouble() / abs(y) // gets sign of y
        val xIsPositive = xSign == 1.0 // checks if x is positive
        val yIsPositive = ySign == 1.0 // checks if y is positive
        return if (xIsPositive && !yIsPositive) {
            1
        } else if (xIsPositive && yIsPositive) {
            2
        } else if (!xIsPositive && !yIsPositive) {
            3
        } else if (!xIsPositive && yIsPositive) {
            4
        } else {
            0
        }
    }

    fun threeFourths(amount: Int): Int {
        return amount / 4 * 3
    }

        fun normDelta(delta: Double): Double {
            return (delta % 180 + 180) % 180
        }

        fun inRange(value: Double, min: Double, max: Double): Boolean {
            return value in min..max
        }

        fun inTolerance(value: Double, value2: Double, tolerance: Double): Boolean {
            return value in value2 - tolerance..value2 + tolerance
        }
        fun angleTo(P1: Point, P2: Point): Double {
            return Math.toDegrees(atan2(P2.y - P1.y, P2.x - P1.x))
        }
        fun distanceTo(P1: Point, P2: Point): Double {
            return hypot(P2.x - P1.x, P2.y - P1.y)
        }
    }
}