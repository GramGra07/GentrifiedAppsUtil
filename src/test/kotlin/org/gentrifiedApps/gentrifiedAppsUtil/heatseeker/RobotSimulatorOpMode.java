package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker;

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.LinearOpModeW;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.DcMotorW;

class RobotSimulatorOpMode extends LinearOpModeW {
    public DcMotorW leftDrive = null;
    public DcMotorW rightDrive = null;


    @Override
    public void runOpMode() {
        leftDrive = hwMap.get(DcMotorW.class, "motor1", 1);
        rightDrive = hwMap.get(DcMotorW.class, "motor2", 2);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive(5.0)) {
            leftDrive.setPower(1);
        }

    }
}