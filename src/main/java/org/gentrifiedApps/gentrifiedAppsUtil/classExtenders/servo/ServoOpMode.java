package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.servo;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
@Disabled
public class ServoOpMode extends LinearOpMode {
    ServoPlus servo = null;

    @Override
    public void runOpMode() {
        servo = new ServoPlus(hardwareMap, "servo", 180);
        waitForStart();
        if (opModeIsActive()) {
            servo.setPosition(90);
        }
    }
}
