package org.gentrifiedApps.gentrifiedAppsUtil.idler;

import static org.gentrifiedApps.gentrifiedAppsUtil.idler.Idler.safeIdle;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
@Disabled
public class IdlerOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        waitForStart();
        if (opModeIsActive()) {
            Idler.safeIdle(3,this, () -> {
                telemetry.addLine("Hello World");
                telemetry.update();
            });
            telemetry.addLine("Done");
            telemetry.update();
        }
    }
}
