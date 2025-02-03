package org.gentrifiedApps.gentrifiedAppsUtil.looptime.demo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController;

@TeleOp
@Disabled

public class LoopTimeDemo extends LinearOpMode {
    public void runOpMode() {
        ElapsedTime elapsedTime = new ElapsedTime();
        LoopTimeController loopTimeController = new LoopTimeController(null);
        waitForStart();
        while (opModeIsActive()) {
            loopTimeController.update();

            loopTimeController.telemetry(telemetry);
            telemetry.update();
        }
    }
}