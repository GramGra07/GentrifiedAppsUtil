package org.gentrifiedApps.gentrifiedAppsUtil.controllers


import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.GamepadPlus

enum class SlowModeDefaults {
    NORMAL
}

/**
 * A class to manage slow modes for a robot.
 * @param slowModeDataList A map of slow mode data to their corresponding keys.
 * @param gamepad The gamepad used to control the robot.
 */
class SlowModeManager(
    internal val slowModeDataList: HashMap<Enum<*>, SlowModeMulti>,
    private val gamepad: GamepadPlus
) {
    companion object {
        @JvmStatic
        fun newInstance(
            slowModeDataList: HashMap<Enum<*>, SlowModeMulti>,
            gamepad: Gamepad
        ): SlowModeManager {
            return SlowModeManager(slowModeDataList, gamepad)
        }
    }

    /**
     * Constructor for SlowModeManager.
     * @param slowModeDataList A map of slow mode data to their corresponding keys.
     * @param gamepad The gamepad used to control the robot -> turns into a Gamepad +
     */
    constructor(
        slowModeDataList: HashMap<Enum<*>, SlowModeMulti>,
        gamepad: Gamepad
    ) : this(slowModeDataList, GamepadPlus(gamepad))

    /**
     * Extremely basic slow mode manager
     * @param gamepad Generic gamepad +
     */
    constructor(gamepad: GamepadPlus) : this(
        hashMapOf(
            SlowModeDefaults.NORMAL to SlowModeMulti(SlowMode.basic(), Button.A),
        ) as HashMap<Enum<*>, SlowModeMulti>,  // Explicit cast
        gamepad
    )

    /**
     * Constructor using a list of pairs
     * @param list A list of pairs of slow mode data and their corresponding keys.
     * @param gamepad The gamepad used to control the robot.
     * Doesn't work well/easily
     */
    constructor(list: List<Pair<Enum<*>, SlowModeMulti>>, gamepad: GamepadPlus) : this(
        list.associate { it.first to it.second } as HashMap<Enum<*>, SlowModeMulti>,
        gamepad
    )

    var currentlyActive: Enum<*>? = null

    init {
        require(slowModeDataList.isNotEmpty(), { "SlowModeDataList must be empty" })
        require(slowModeDataList.size < 10, { "SlowModeDataList must be less than 10" })
        if (slowModeDataList.size > 1) {
            currentlyActive = null
        } else if (slowModeDataList.size == 1) {
            currentlyActive = slowModeDataList.keys.first()
        }
    }

    /**
     * Applies the slow mode to the given value.
     * @param value The value to apply the slow mode to.
     * @return The value after applying the slow mode.
     */
    fun apply(value: Double): Double {
        update()
        var result = value
        slowModeDataList[currentlyActive]?.let {
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
        update()
        var result = drivePowerCoefficients
        slowModeDataList[currentlyActive]?.let {
            result = it.slowModeData.apply(result)
        }
        return result
    }

    /**
     * Updates the slow mode manager.
     */
    fun update() {
        for (slowModeMulti in slowModeDataList) {
            if (slowModeMulti.value.slowMode) {
                if (slowModeMulti.value.changed()) {
                    currentlyActive = slowModeMulti.key
                }
                if (currentlyActive != slowModeMulti.key) {
                    slowModeMulti.value.deactivate()
                }
            }
            slowModeMulti.value.update(gamepad)
        }
    }

    /**
     * Reports the current slow mode to the telemetry.
     * @param telemetry The telemetry to report to.
     */
    fun telemetry(telemetry: Telemetry) {
        currentlyActive?.let {
            slowModeDataList[it]?.report(telemetry)
        }
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
    internal val activeButton: Button,
    internal val deactiveButton: Button
) {
    constructor(slowModeData: SlowMode, activeButton: Button) : this(
        slowModeData,
        activeButton,
        activeButton
    )

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

    /**
     * Updates the slow mode.
     * @param gamepad The gamepad used to control the robot.
     */
    fun update(gamepad: GamepadPlus) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime > debounceTime) {
            if (gamepad.buttonJustPressed(activeButton) && !slowMode) {
                slowMode = true
                lastUpdateTime = currentTime
            } else if (gamepad.buttonJustPressed(deactiveButton) && slowMode) {
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
            return SlowModeMulti(SlowMode.basic(), Button.A)
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