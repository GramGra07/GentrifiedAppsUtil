package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics

import org.gentrifiedApps.gentrifiedAppsUtil.dataStorage.DataStorage
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Target2D

/**
 * A class to represent a point in 2D space
 * @param x The x coordinate of the point
 * @param y The y coordinate of the point
 * @constructor Creates a point with the given x and y coordinates
 */
data class Point(var x: Double, var y: Double) {
    fun store() {
        DataStorage.setPose(this.toTarget2D())
    }

    fun toTarget2D(): Target2D {
        return Target2D(this.x, this.y, 0.0)
    }

    operator fun times(b: Double): Point {
        return Point(this.x * b, this.y * b)
    }

    operator fun div(b: Double): Point {
        return Point(this.x / b, this.y / b)
    }

    operator fun plus(b: Point): Point {
        return Point(this.x + b.x, this.y + b.y)
    }

    operator fun minus(b: Point): Point {
        return Point(this.x - b.x, this.y - b.y)
    }

    operator fun timesAssign(b: Double) {
        this.x *= b
        this.y *= b
    }

    operator fun divAssign(b: Double) {
        this.x /= b
        this.y /= b
    }

    operator fun plusAssign(b: Point) {
        this.x += b.x
        this.y += b.y
    }

    operator fun minusAssign(b: Point) {
        this.x -= b.x
        this.y -= b.y
    }
}