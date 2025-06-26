package org.gentrifiedApps.gentrifiedAppsUtil.controllers


import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients

enum class SlowModeDefaults {
    NORMAL
}

/**
 * A class to manage slow modes for a robot.
 * @param slowModeDataList A map of slow mode data to their corresponding keys.
 * @param gamepad The gamepad used to control the robot.
 */
class SlowModeManager(
    internal val slowModeDataList: SlowModeMulti
) {

    /**
     * Applies the slow mode to the given value.
     * @param value The value to apply the slow mode to.
     * @return The value after applying the slow mode.
     */
    fun apply(value: Double): Double {
        var result = value
        slowModeDataList.let {
            result = it.apply(result)
        }
        return result
    }

    /**
     * Applies the slow mode to the given drive power coefficients.
     * @param drivePowerCoefficients The drive power coefficients to apply the slow mode to.
     * @return The drive power coefficients after applying the slow mode.
     */
    fun apply(drivePowerCoefficients: DrivePowerCoefficients): DrivePowerCoefficients {
        var result = drivePowerCoefficients
        slowModeDataList.let {
            result = it.slowModeData.apply(result)
        }
        return result
    }

    /**
     * Updates the slow mode manager.
     */
    fun update(onFunc: Boolean, offFunc: Boolean) {
        slowModeDataList.update(onFunc, offFunc)
    }

    /**
     * Reports the current slow mode to the telemetry.
     * @param telemetry The telemetry to report to.
     */
    fun telemetry(telemetry: Telemetry) {
        slowModeDataList.report(telemetry)
    }
}

/**
 * A class to represent a slow mode multi selector, or 1
 * @param slowModeData The slow mode data.
 * @param activeButton The active button.
 * @param deactiveButton The deactive button.
 */
data class SlowModeMulti(
    val slowModeData: SlowMode,
) {
    var slowMode: Boolean = false
    var lastSlowMode: Boolean = false
    fun deactivate() {
        slowMode = false
    }

    /**
     * Applies the slow mode to the given value.
     * @param value The value to apply the slow mode to.
     * @return The value after applying the slow mode.
     */
    fun apply(value: Double): Double {
        return if (slowMode) {
            slowModeData.apply(value)
        } else {
            value
        }
    }

    fun changed(): Boolean {
        return slowMode != lastSlowMode
    }

    private var lastUpdateTime: Long = 0
    private val debounceTime: Long = 200 // 200 milliseconds

    fun update(onFunc: Boolean, offFunc: Boolean) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime > debounceTime) {
            if (onFunc && !slowMode) {
                slowMode = true
                lastUpdateTime = currentTime
            } else if (offFunc && slowMode) {
                slowMode = false
                lastUpdateTime = currentTime
            } else {
                lastSlowMode = slowMode
            }
        }
    }

    /**
     * Reports the current slow mode to the telemetry.
     * @param telemetry The telemetry to report to.
     */
    fun report(telemetry: Telemetry) {
        telemetry.addData("SlowMode", "1/${slowModeData.slowModeFactor}, $slowMode")
    }


    companion object {
        @JvmStatic
                /**
                 * Creates a basic slow mode multi with a slow mode factor of 2.0 and the active button as A.
                 */
        fun basic(): SlowModeMulti {
            return SlowModeMulti(SlowMode.basic())
        }
    }
}

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