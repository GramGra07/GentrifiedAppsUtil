package org.gentrifiedApps.gentrifiedAppsUtil.controllers


import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients

/**
 * A class to represent a slow mode.
 * @param slowModeFactor The slow mode factor.
 */
data class SlowMode(val slowModeFactor: Double) {
    companion object {
        @JvmStatic
        fun basic(): SlowMode {
            return SlowMode(2.0)
        }

        @JvmStatic
                /**
                 * Creates a basic slow mode with a slow mode factor of 1.0.
                 */
        fun one(): SlowMode {
            return SlowMode(1.0)
        }

        @JvmStatic
        fun of(slowModeFactor: Double): SlowMode {
            return SlowMode(slowModeFactor)
        }
    }

    init {
        require(slowModeFactor >= 1.0, { "SlowModeFactor must be greater than or equal to 1.0" })
    }

    fun apply(value: Double): Double {
        return value / slowModeFactor
    }

    fun apply(drivePowerCoefficients: DrivePowerCoefficients): DrivePowerCoefficients {
        return drivePowerCoefficients / slowModeFactor
    }
}