package org.gentrifiedApps.gentrifiedAppsUtil.decode

import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Angle
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class ProjectileMotionCalculator(
    var launchHeight: Double,
    var launchAngle: Angle?,
    var initialVelocity: Double?,
    var range: Double?
) {
    val targetHeight = 5
    val g = 9.81
    private val dy: Double get() = targetHeight - launchHeight

    fun calculateTimeOfFlight(): Double {
        val v0 = requireNotNull(initialVelocity)
        val theta = requireNotNull(launchAngle).toRadians()
        val vy0 = v0 * sin(theta)
        val disc = vy0 * vy0 - 2.0 * g * dy
        require(disc >= 0.0) { "Target height not reachable for given v0 and angle." }
        // Use the '+' root for the landing time
        return (vy0 + sqrt(disc)) / g
    }

    fun calculateRange(): Double {
        val v0 = requireNotNull(initialVelocity)
        val theta = requireNotNull(launchAngle).toRadians()
        return v0 * cos(theta) * calculateTimeOfFlight()
    }

    // Returns the two valid angles (low, high) if both exist; one if grazing; throws if impossible.
    fun calculateRequiredLaunchAngles(): List<Angle> {
        val v0 = requireNotNull(initialVelocity)
        val R = requireNotNull(range)
        require(R > 0) { "Range must be positive." }

        val v0sq = v0 * v0
        val inner = v0sq * v0sq - g * (g * R * R + 2.0 * dy * v0sq)
        require(inner >= 0.0) { "No real launch angle achieves the target with given v0 and R." }

        val root = sqrt(inner)
        val denom = g * R

        fun ang(numer: Double) = Angle(Math.toDegrees(atan(numer / denom)))

        // tanθ = (v0^2 ± sqrt(...)) / (gR)
        val low = ang(v0sq - root)  // lower angle solution
        val high = ang(v0sq + root)  // higher angle solution

        return if (root == 0.0) listOf(low) else listOf(low, high)
    }

    fun calculateRequiredInitialVelocity(): Double {
        val R = requireNotNull(range)
        val theta = requireNotNull(launchAngle).toRadians()
        require(R > 0) { "Range must be positive." }

        val cosT = cos(theta)
        val denom = 2.0 * cosT * cosT * (R * tan(theta) - dy)
        require(denom > 0.0) {
            "No physical solution for given R, θ, and heights (check that R·tanθ > Δy)."
        }

        return sqrt((g * R * R) / denom)
    }

    fun willLand(): Boolean {
        // detects if given all parameters, the projectile will land at the target height and range
        val v0 = requireNotNull(initialVelocity)
        val theta = requireNotNull(launchAngle).toRadians()
        val R = requireNotNull(range)
        val time = calculateTimeOfFlight()
        val xAtTime = v0 * cos(theta) * time
        val yAtTime = launchHeight + v0 * sin(theta) * time - 0.5 * g * time.pow(2)
        val epsilon = 0.01
        return (kotlin.math.abs(xAtTime - R) < epsilon) && (kotlin.math.abs(yAtTime - targetHeight) < epsilon)
    }
}