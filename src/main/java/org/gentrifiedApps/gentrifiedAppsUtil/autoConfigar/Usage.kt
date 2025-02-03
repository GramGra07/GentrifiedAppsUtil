package org.gentrifiedApps.gentrifiedAppsUtil.autoConfigar

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.gentrifiedApps.gentrifiedAppsUtil.gamepad.Button

class Usage(passButton: Button, buttonMap: HashMap<String, ButtonConfig>,
            private val initFunc:Runnable, private val allFuncs:List<funcStore>, private val passConditions:List<PassCondition>):LinearOpMode() {
    private val autoConfigar = AutoConfigar(
        this,
        gamepad1,
        passButton,
        buttonMap
    )
    private val autoRunner = AutoRunner(allFuncs)
    override fun runOpMode() {
        initFunc.run()
        require(allFuncs.size == passConditions.size){
            "The number of functions and pass conditions must be the same"
        }
        waitForStart()
        if (opModeIsActive()) {
            for (i in allFuncs.indices) {
                autoRunner.start(i)
                autoConfigar.runPassCondition(passConditions[i])
                autoRunner.end(i)
            }
            autoConfigar.report()
        }
    }
}