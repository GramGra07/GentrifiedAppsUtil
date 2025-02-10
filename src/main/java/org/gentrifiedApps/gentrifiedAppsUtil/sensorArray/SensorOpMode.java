package org.gentrifiedApps.gentrifiedAppsUtil.sensorArray;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.Button;
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.GamepadPair;
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad.GamepadPlus;

@TeleOp
@Disabled
public class SensorOpMode extends LinearOpMode {
    SensorArray sensorArray = new SensorArray();

    @Override
    public void runOpMode() {
        sensorArray.addSensor(new Sensor("sensor1",SensorType.DIST,()-> hardwareMap.get(DistanceSensor.class, "sensor1"),1));
        waitForStart();
        while (opModeIsActive()) {
            sensorArray.readAllLoopSaving();


            sensorArray.allTelemetry(telemetry);
            telemetry.update();
            ////// or

            sensorArray.readAllLoopSaving();
            sensorArray.autoLoop(1);
            sensorArray.allTelemetry(telemetry);
            telemetry.update();
        }
    }
}
