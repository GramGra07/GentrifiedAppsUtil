package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot

class MathFunctions {
    companion object {
        /**
         * Returns the average of an array of values
         * @param values The values to get the average of
         * @return The average of the values
         */
        fun averageOf(values: DoubleArray): Double {
            return values.average()
        }

        /**
         * Returns the quadrant of a point
         * @param pose The point to get the quadrant of
         * @return The quadrant of the point
         * 1 | 2
         * -----
         * 3 | 4
         */
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
                4
            } else if (xIsPositive && yIsPositive) {
                2
            } else if (!xIsPositive && !yIsPositive) {
                3
            } else if (!xIsPositive && yIsPositive) {
                1
            } else {
                0
            }
        }

        /**
         * Returns three-fourths of a number
         * @param amount The number to get three-fourths of
         * @return Three-fourths of the number
         */
        fun threeFourths(amount: Int): Int {
            return amount / 4 * 3
        }

        /**
         * Normalizes a delta to be between -180 and 180
         * @param delta The delta to normalize
         * @return The normalized delta
         */
        fun normDelta(delta: Double): Double {
            return (delta % 180 + 180) % 180
        }

        /**
         * Returns if a value is within a range
         * @param value The value to check
         * @param min The minimum value
         * @param max The maximum value
         */
        fun inRange(value: Double, min: Double, max: Double): Boolean {
            return value in min..max
        }

        /**
         * Returns if a value is within a tolerance of another value
         * @param value The value to check
         * @param value2 The value to check against
         * @param tolerance The tolerance
         * @return Boolean if the value is within the tolerance of the other value
         */

        fun inTolerance(value: Double, value2: Double, tolerance: Double): Boolean {
            return value in value2 - tolerance..value2 + tolerance
        }

        fun inTolerance(value: Int, value2: Int, tolerance: Int): Boolean {
            return inTolerance(value.toDouble(), value2.toDouble(), tolerance.toDouble())
        }

        /**
         * Returns the angle between two points in degrees
         * @param P1 The first point
         * @param P2 The second point
         */
        fun angleTo(P1: Point, P2: Point): Double {
            return Math.toDegrees(atan2(P2.y - P1.y, P2.x - P1.x))
        }

        /**
         * Returns the distance between two points
         * @param P1 The first point
         * @param P2 The second point
         */
        fun distanceTo(P1: Point, P2: Point): Double {
            return hypot(P2.x - P1.x, P2.y - P1.y)
        }

        fun angleBetween(P1: Point, P2: Point): Double {
            return Math.toDegrees(atan2(P2.y - P1.y, P2.x - P1.x))
        }

        fun getError(set: Double, current: Double): Double {
            return abs(set - current)
        }

        fun clip(input: Double, min: Double, max: Double): Double {
            return when {
                input < min -> min
                input > max -> max
                else -> input
            }
        }

        fun round(input: Double, places: Int): Double {
            require(places >= 0) { "Decimal places must be non-negative." }
            val factor = Math.pow(10.0, places.toDouble())
            return kotlin.math.round(input * factor) / factor
        }

        fun ticksToInches(ticks: Int, ticksPerIn: Double): Double {
            return ticks.toDouble() / ticksPerIn
        }
    }
}