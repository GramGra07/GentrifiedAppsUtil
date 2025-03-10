package org.gentrifiedApps.gentrifiedAppsUtil.sensorArray;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;

@TeleOp
@Disabled
public class SensorOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        SensorArray sensorArray = new SensorArray(hardwareMap);
        sensorArray.addSensor(new Sensor("sensor1", SensorType.DIST, 1));
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
