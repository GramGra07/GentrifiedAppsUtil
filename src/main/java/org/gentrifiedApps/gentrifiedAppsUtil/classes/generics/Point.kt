package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics

/**
 * A class to represent a point in 2D space
 * @param x The x coordinate of the point
 * @param y The y coordinate of the point
 * @constructor Creates a point with the given x and y coordinates
 */
data class Point(var x: Double, var y: Double) {
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