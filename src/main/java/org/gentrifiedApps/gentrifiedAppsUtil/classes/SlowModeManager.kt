package org.gentrifiedApps.gentrifiedAppsUtil.classes


import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.GamepadPlus
import org.gentrifiedApps.gentrifiedAppsUtil.drive.DrivePowerCoefficients
import kotlin.collections.get

enum class SlowModeDefaults{
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
    constructor (list: List<*>, gamepad: GamepadPlus) : this(
        list as HashMap<Enum<*>, SlowModeMulti>,  // Explicit cast
        gamepad
    )

    var currentlyActive: Enum<*>? = null

    fun apply(value: Double): Double {
        var result = value
        slowModeDataList[currentlyActive]?.let {
            result = it.apply(result)
        }
        return result
    }

    fun apply(drivePowerCoefficients: DrivePowerCoefficients): DrivePowerCoefficients {
        var result = drivePowerCoefficients
        slowModeDataList[currentlyActive]?.let {
            result = it.slowModeData.apply(result)
        }
        return result
    }

    fun update(){
        for (slowModeMulti in slowModeDataList) {
            if (slowModeMulti.value.slowMode){
                currentlyActive = slowModeMulti.key
            }
            slowModeMulti.value.update(gamepad)
        }
    }

    fun telemetry(telemetry: Telemetry) {
        for (slowModeMulti in slowModeDataList) {
            slowModeMulti.value.report(telemetry)
        }
    }
}
data class SlowModeMulti(val slowModeData: SlowMode, val activeButton: Button, val deactiveButton: Button) {
    constructor(slowModeData: SlowMode, activeButton: Button) : this(slowModeData, activeButton, activeButton)
    var slowMode:Boolean = false
    fun apply(value: Double): Double {
        return if (slowMode) {
            slowModeData.apply(value)
        } else {
            value
        }
    }
    fun update(gamepad: GamepadPlus){
        if (gamepad.buttonJustPressed(activeButton)) {
            slowMode = true
        } else if (gamepad.buttonJustPressed(deactiveButton)) {
            slowMode = false
        }
    }
    fun report(telemetry: Telemetry){
        if (slowMode) {
            telemetry.addData("SlowMode", "${slowModeData.slowModeFactor}")
        }
    }


    companion object{
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