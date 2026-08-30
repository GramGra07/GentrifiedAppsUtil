package com.dacodingbeast.pidtuners.utilities.MathFunctions

class MathFunctions {
    companion object{
        fun clamp(value: Double, min: Double, max: Double): Double {
            return when {
                value < min -> min
                value > max -> max
                else -> value
            }
        }
        fun in_tolerance(value:Double, target:Double, tolerance:Double):Boolean {
            return (value >= target - tolerance && value <= target + tolerance)
        }
    }
}