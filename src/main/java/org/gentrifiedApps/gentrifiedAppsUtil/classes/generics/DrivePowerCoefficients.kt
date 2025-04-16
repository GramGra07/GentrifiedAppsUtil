package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Quadruple
import org.gentrifiedApps.gentrifiedAppsUtil.classes.SlowModeManager
import org.gentrifiedApps.gentrifiedAppsUtil.motion.profiles.MultiSlewLimiter

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
    companion object {
        fun zeros(): DrivePowerCoefficients {
            return DrivePowerCoefficients(0.0, 0.0, 0.0, 0.0)
        }
    }

    fun applySlowMode(slowModeManager: SlowModeManager): DrivePowerCoefficients {
        return slowModeManager.apply(this)
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

    fun notZero(): Boolean {
        return this != zeros()
    }

    fun applySlew(rLimiter: MultiSlewLimiter, quad:Quadruple<String>) : DrivePowerCoefficients{
        require(rLimiter.length() == 4) { "Slew limiter must have 4 values" }
        val frontLeft = rLimiter.calculate(quad.first,this.frontLeft)
        val frontRight = rLimiter.calculate(quad.second,this.frontRight)
        val backLeft = rLimiter.calculate(quad.third,this.backLeft)
        val backRight = rLimiter.calculate(quad.fourth,this.backRight)

        return DrivePowerCoefficients(frontLeft, frontRight, backLeft, backRight)
    }
}