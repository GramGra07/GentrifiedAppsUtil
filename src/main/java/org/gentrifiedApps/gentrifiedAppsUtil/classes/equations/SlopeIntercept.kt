package org.gentrifiedApps.gentrifiedAppsUtil.classes.equations

data class SlopeIntercept(var m: Number, var b: Number) {
    constructor(m: Number) : this(m, 0)

    /**
     * Returns the y value for a given x value using the slope-intercept form of a line.
     * @param x The x value to calculate the y value for.
     * @return The y value for the given x value.
     */
    fun getY(x: Number): Double {
        return (m.toDouble() * x.toDouble()) + b.toDouble()
    }

    companion object {
        @JvmStatic
        fun autoLevel1DF(slope: Number): SlopeIntercept {
            return SlopeIntercept(slope, 90)
        }

        @JvmStatic
        fun fromSlopeAndYIntercept(slope: Number, yIntercept: Number): SlopeIntercept {
            return SlopeIntercept(slope, yIntercept)
        }

        @JvmStatic
        fun fromPoints(x1: Number, y1: Number, x2: Number, y2: Number): SlopeIntercept {
            val m = (y2.toDouble() - y1.toDouble()) / (x2.toDouble() - x1.toDouble())
            val b = y1.toDouble() - (m * x1.toDouble())
            return SlopeIntercept(m, b)
        }

        @JvmStatic
        fun fromPointAndSlope(x: Number, y: Number, slope: Number): SlopeIntercept {
            val b = y.toDouble() - (slope.toDouble() * x.toDouble())
            return SlopeIntercept(slope, b)
        }

        @JvmStatic
        fun zeros(): SlopeIntercept {
            return SlopeIntercept(0, 0)
        }
    }

    override operator fun equals(other: Any?): Boolean {
        if (other !is SlopeIntercept) {
            return false
        }
        return m == other.m && b == other.b
    }
}
