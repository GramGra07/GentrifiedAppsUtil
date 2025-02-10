package org.gentrifiedApps.gentrifiedAppsUtil.initMovement;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
@Disabled
public class InitOpMode extends LinearOpMode {
    InitMovementController initMovementController;

    @Override
    public void runOpMode() {
        initMovementController = new InitMovementController(gamepad1, gamepad2);
        waitForStart();
        while (opModeIsActive()) {
            initMovementController.checkHasMovedOnInit();
            if (initMovementController.hasMovedOnInit()) {
                telemetry.addData("Status", "Moved");
                telemetry.update();
            } else {
                telemetry.addData("Status", "Not Moved");
                telemetry.update();
            }
        }
    }
}
