package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot;

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.DcMotorW;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.LinearOpModeW;

public class RobotSimulatorOpMode extends LinearOpModeW {
    public DcMotorW leftDrive = null;
    public DcMotorW rightDrive = null;


    @Override
    public void runOpMode() {
        leftDrive = hwMap.get(DcMotorW.class, "motor1", 1);
        rightDrive = hwMap.get(DcMotorW.class, "motor2", 2);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

        }

    }
}