package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robotSims;

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.LinearOpModeW;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.DcMotorW;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.DriverW;

public class BasicRobotSimulatorOpMode extends LinearOpModeW {
    public DcMotorW leftDrive = null;
    public DcMotorW rightDrive = null;
    public DriverW driver = null;


    @Override
    public void runOpMode() {
        driver = new DriverW(this, "motor1", "motor2", "motor3", "motor4");
        leftDrive = hwMap.get(DcMotorW.class, "motor1");
        rightDrive = hwMap.get(DcMotorW.class, "motor2");
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive(5.0) && !isStopRequested()) {
            leftDrive.setPower(1);
        }

    }
}