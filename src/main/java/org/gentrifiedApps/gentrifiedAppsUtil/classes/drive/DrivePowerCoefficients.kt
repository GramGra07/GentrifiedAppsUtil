package org.gentrifiedApps.gentrifiedAppsUtil.classes.drive

import org.gentrifiedApps.gentrifiedAppsUtil.classes.MathFunctions.Companion.clip
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Quadruple
import org.gentrifiedApps.gentrifiedAppsUtil.controllers.SlowMode
import org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles.MultiSlewLimiter
import kotlin.math.abs

/**
 * A class to hold the coefficients for field centric driving.
 * @param frontLeft The coefficient for the front left motor.
 * @param frontRight The coefficient for the front right motor.
 * @param backLeft The coefficient for the back left motor.
 * @param backRight The coefficient for the back right motor.
 */
data class DrivePowerCoefficients(
    val frontLeft: Double,
    val frontRight: Double,
    val backLeft: Double,
    val backRight: Double
) {
    constructor(one: Double) : this(one, one, one, one)

    companion object {
        fun zeros(): DrivePowerCoefficients {
            return DrivePowerCoefficients(0.0, 0.0, 0.0, 0.0)
        }

        fun of(one: Double): DrivePowerCoefficients {
            return DrivePowerCoefficients(one, one, one, one)
        }
    }

    /**
     * Applies a constraint to the coefficients.
     * @param constraint The constraint to apply.
     * @return The coefficients with the constraint applied.
     * @see DrivePowerConstraint
     * @see DrivePowerCoefficients
     */
    fun applyConstraint(constraint: DrivePowerCoefficients): DrivePowerCoefficients {
        return DrivePowerCoefficients(
            clip(frontLeft, -constraint.frontLeft, constraint.frontLeft),
            clip(frontRight, -constraint.frontRight, constraint.frontRight),
            clip(backLeft, -constraint.backLeft, constraint.backLeft),
            clip(backRight, -constraint.backRight, constraint.backRight)
        )
    }

    /**
     * Applies a constraint to the coefficients.
     * @param slowModeManager The constraint to apply.
     * @return The coefficients with the constraint applied.
     * @see DrivePowerConstraint
     * @see DrivePowerCoefficients
     */
    fun applySlowMode(slowModeManager: SlowMode): DrivePowerCoefficients {
        return slowModeManager.apply(this)
    }

    /**
     * Applies a slow mode to the coefficients.
     * @param divisor The divisor to apply.
     * @return The coefficients with the slow mode applied.
     * @see DrivePowerCoefficients
     */
    fun applySlowMode(divisor: Double): DrivePowerCoefficients {
        return this / divisor
    }

    operator fun times(b: Double): DrivePowerCoefficients {
        return DrivePowerCoefficients(
            this.frontLeft * b,
            this.frontRight * b,
            this.backLeft * b,
            this.backRight * b
        )
    }

    operator fun div(b: Double): DrivePowerCoefficients {
        return DrivePowerCoefficients(
            this.frontLeft / b,
            this.frontRight / b,
            this.backLeft / b,
            this.backRight / b
        )
    }

    operator fun plus(b: DrivePowerCoefficients): DrivePowerCoefficients {
        return DrivePowerCoefficients(
            this.frontLeft + b.frontLeft,
            this.frontRight + b.frontRight,
            this.backLeft + b.backLeft,
            this.backRight + b.backRight
        )
    }

    fun clip(min: Double, max: Double): DrivePowerCoefficients {
        return DrivePowerCoefficients(
            clip(this.frontLeft, min, max),
            clip(this.frontRight, min, max),
            clip(this.backLeft, min, max),
            clip(this.backRight, min, max)
        )
    }

    fun notZero(): Boolean {
        return this != zeros()
    }

    fun max(): Double {
        return maxOf(frontLeft, frontRight, backLeft, backRight)
    }

    fun min(): Double {
        return minOf(frontLeft, frontRight, backLeft, backRight)
    }

    fun normalizeTo1(): DrivePowerCoefficients {
        val max = max()
        return this / max
    }

    /**
     * Applies a slew rate limiter to the coefficients.
     * @param rLimiter The slew rate limiter to apply.
     * @param quad The quadruple of values to apply the slew rate limiter to.
     * @return The coefficients with the slew rate limiter applied.
     */
    fun applySlew(rLimiter: MultiSlewLimiter, quad: Quadruple<String>): DrivePowerCoefficients {
        require(rLimiter.length() == 4) { "Slew limiter must have 4 values" }
        val frontLeft = rLimiter.calculate(quad.first, this.frontLeft)
        val frontRight = rLimiter.calculate(quad.second, this.frontRight)
        val backLeft = rLimiter.calculate(quad.third, this.backLeft)
        val backRight = rLimiter.calculate(quad.fourth, this.backRight)

        return DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
    }


    fun applyAvoidance(drivePowerCoefficients: DrivePowerCoefficients): DrivePowerCoefficients {
        return (this + drivePowerCoefficients).clip(0.0, 1.0)
    }

    internal fun applyDriftNormalizer(tolerance: Double): Quadruple<Double> {
        val min = min()
        val max = max()
        val dif = abs(max - min)
        if (dif >= tolerance) {
            val frontLeft = 1 + ((dif * (((1 - frontLeft) / dif) - 1)))
            val frontRight = 1 + ((dif * (((1 - frontRight) / dif) - 1)))
            val backLeft = 1 + ((dif * (((1 - backLeft) / dif) - 1)))
            val backRight = 1 + ((dif * (((1 - backRight) / dif) - 1)))
            return Quadruple(frontLeft, frontRight, backLeft, backRight)
        }
        return Quadruple(0.0)
    }
}