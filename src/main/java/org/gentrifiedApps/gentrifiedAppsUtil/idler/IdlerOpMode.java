package org.gentrifiedApps.gentrifiedAppsUtil.idler;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
@Disabled
public class IdlerOpMode extends LinearOpMode {
    Idler idler = new Idler();
        @Override
        public void runOpMode() {
            waitForStart();
            if (opModeIsActive()) {
                idler.safeIdle(3,()->{telemetry.addLine("Hello World");});
            }
        }
}
