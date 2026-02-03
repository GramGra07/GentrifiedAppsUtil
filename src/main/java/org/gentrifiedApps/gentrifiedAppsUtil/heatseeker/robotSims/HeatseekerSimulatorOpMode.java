package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robotSims;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.Path;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PathBuilder;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.LinearOpModeW;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.DriverW;

public class HeatseekerSimulatorOpMode extends LinearOpModeW {
    public DriverW driver = new DriverW();
    public Path p = new PathBuilder().forward(10).back(10).build();


    @Override
    public void runOpMode() {
        driver.setBackLeftName("bl").setBackRightName("br").setFrontLeftName("fl").setFrontRightName("fr")
                .reverseBackLeft()
                .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
                .setPathDeceleration(2, 1)

                .build(this);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive(5.0) && !isStopRequested()) {
            driver.setWheelPower(driver.findWheelVectors(1, 0, 0));
        }

    }
}