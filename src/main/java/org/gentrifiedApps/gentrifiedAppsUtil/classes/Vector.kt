package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Target2D
import java.io.InvalidClassException
import kotlin.math.sqrt

open class Vector(open val a: Double, open val b: Double, val c: Double) {
    operator fun plus(other: Vector) = Vector(a + other.a, b + other.b, c + other.c)
    operator fun minus(other: Vector) = Vector(a - other.a, b - other.b, c - other.c)
    operator fun times(other: Vector) = dotProduct(other)
    operator fun times(scalar: Double) = Vector(a * scalar, b * scalar, c * scalar)
    override operator fun equals(other: Any?): Boolean {
        if (other !is Vector) {
            throw InvalidClassException("Vector compared with type: " + (other?.javaClass.toString()))
        }
        return (a == other.a && b == other.b && c == other.c)
    }

    fun magnitude(): Double {
        return sqrt(a * a + b * b + c * c)
    }

    fun crossProduct(other: Vector): Vector {
        return Vector(
            b * other.c - c * other.b,
            c * other.a - a * other.c,
            a * other.b - b * other.a
        )
    }

    fun dotProduct(other: Vector): Double {
        return a * other.a + b * other.b + c * other.c
    }

    companion object {
        fun of(p1: Target2D, p2: Target2D): Vector {
            return Vector(p2.x - p1.x, p2.y - p1.y, 0.0)
        }
    }

    override fun hashCode(): Int {
        var result = a.hashCode()
        result = 31 * result + b.hashCode()
        result = 31 * result + c.hashCode()
        return result
    }

    fun unitVec(): Vector {
        val mag = magnitude()
        return Vector(a / mag, b / mag, c / mag)
    }
}