package org.gentrifiedApps.gentrifiedAppsUtil.autoConfigar

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad
import org.gentrifiedApps.gentrifiedAppsUtil.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.gamepad.GamepadOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.gamepad.GamepadPlus

class AutoConfigar(
    private val opMode: LinearOpMode, gamepadController:Gamepad,
    private val passCondition:Button, private val buttons:HashMap<String,ButtonConfig>,private val gainIncrease:Button = Button.RIGHT_BUMPER, private val gainDecrease:Button = Button.LEFT_BUMPER) {
    private var gamepad = GamepadPlus(gamepadController)
    private var gainController = GainController(
        buttons, gamepad, gainDecrease, gainIncrease)

    private fun mainLoop(){

        gainController.loop()

        gamepad.sync()
        opMode.telemetry.addData("Gain", gainController.gain)
        opMode.telemetry.update()
    }
    fun runPassCondition(pass: PassCondition){
        while(!gamepad.readBooleanButtonFromController(passCondition)){
            if (gamepad.readBooleanButtonFromController(passCondition)){
                break
            }
            pass.secondaryCondition?.run()
            mainLoop()
            if (gainController.gainChanged( pass.gainChanged)){
                pass.runOnChange.run()
                gainController.lastGain()
            }
            pass.runConstant?.run()
        }
        gainController.end( pass.gainChanged)
    }
    fun report(){
        while (opMode.opModeIsActive() &&!opMode.isStopRequested){
            gainController.finalGains.forEach(){
                opMode.telemetry.addData(it.key,it.value)
            }
            opMode.telemetry.update()
        }
    }

}
data class PassCondition(val runOnChange:Runnable,val gainChanged:List<String>,val runConstant:Runnable? = null,val secondaryCondition:Runnable? = null)