package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robotSims;

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.LinearOpModeW;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.DcMotorW;

public class BasicRobotSimulatorOpMode extends LinearOpModeW {
    public DcMotorW leftDrive = null;
    public DcMotorW rightDrive = null;

    @Override
    public void runOpMode() {
        leftDrive = hardwareMap.get(DcMotorW.class, "motor1");
        rightDrive = hardwareMap.get(DcMotorW.class, "motor2");
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive(5.0) && !isStopRequested()) {
            leftDrive.setPower(1);
        }
    }
}