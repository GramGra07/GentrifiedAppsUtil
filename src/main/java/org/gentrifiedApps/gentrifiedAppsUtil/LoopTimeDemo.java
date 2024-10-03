package org.gentrifiedApps.gentrifiedAppsUtil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
//@Disabled

public class LoopTimeDemo extends LinearOpMode {
    public void runOpMode() {
        ElapsedTime elapsedTime = new ElapsedTime();
        LoopTimeController loopTimeController = new LoopTimeController(elapsedTime,null);
        waitForStart();
        while (opModeIsActive()) {
            loopTimeController.update();

            loopTimeController.telemetry(telemetry);
            telemetry.update();
        }
    }
}