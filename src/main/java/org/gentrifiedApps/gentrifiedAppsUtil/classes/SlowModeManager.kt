package org.gentrifiedApps.gentrifiedAppsUtil.classes


import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.GamepadPlus
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients

enum class SlowModeDefaults {
    NORMAL
}

class SlowModeManager(
    val slowModeDataList: HashMap<Enum<*>, SlowModeMulti>,
    val gamepad: GamepadPlus
) {
    constructor(
        slowModeDataList: HashMap<Enum<*>, SlowModeMulti>,
        gamepad: Gamepad
    ) : this(slowModeDataList, GamepadPlus(gamepad))

    constructor(gamepad: GamepadPlus) : this(
        hashMapOf(
            SlowModeDefaults.NORMAL to SlowModeMulti(SlowMode.basic(), Button.A),
        ) as HashMap<Enum<*>, SlowModeMulti>,  // Explicit cast
        gamepad
    )

    /**
     *
     * Doesn't work well/easily
     */
    constructor(list: List<Pair<Enum<*>, SlowModeMulti>>, gamepad: GamepadPlus) : this(
        list.associate { it.first to it.second } as HashMap<Enum<*>, SlowModeMulti>,
        gamepad
    )

    var currentlyActive: Enum<*>? = null

    init {
        require(slowModeDataList.isNotEmpty(), { "SlowModeDataList must be empty" })
        if (slowModeDataList.size > 1) {
            currentlyActive = null
        } else if (slowModeDataList.size == 1) {
            currentlyActive = slowModeDataList.keys.first()
        }
    }

    fun apply(value: Double): Double {
        update()
        var result = value
        slowModeDataList[currentlyActive]?.let {
            result = it.apply(result)
        }
        return result
    }

    fun apply(drivePowerCoefficients: DrivePowerCoefficients): DrivePowerCoefficients {
        update()
        var result = drivePowerCoefficients
        slowModeDataList[currentlyActive]?.let {
            result = it.slowModeData.apply(result)
        }
        return result
    }

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

    fun telemetry(telemetry: Telemetry) {
        currentlyActive?.let {
            slowModeDataList[it]?.report(telemetry)
        }
    }
}

data class SlowModeMulti(
    val slowModeData: SlowMode,
    val activeButton: Button,
    val deactiveButton: Button
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

    fun report(telemetry: Telemetry) {
        telemetry.addData("SlowMode", "1/${slowModeData.slowModeFactor}, $slowMode")
    }


    companion object {
        @JvmStatic
        fun basic(): SlowModeMulti {
            return SlowModeMulti(SlowMode.basic(), Button.A)
        }
    }
}

data class SlowMode(val slowModeFactor: Double) {
    companion object {
        @JvmStatic
        fun basic(): SlowMode {
            return SlowMode(2.0)
        }

        @JvmStatic
        fun one(): SlowMode {
            return SlowMode(1.0)
        }
    }

    fun apply(value: Double): Double {
        return value / slowModeFactor
    }

    fun apply(drivePowerCoefficients: DrivePowerCoefficients): DrivePowerCoefficients {
        return drivePowerCoefficients / slowModeFactor
    }
}