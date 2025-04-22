package org.gentrifiedApps.gentrifiedAppsUtil.sensorArray;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.gentrifiedApps.gentrifiedAppsUtil.classes.analogEncoder.AnalogEncoder;

@TeleOp
//@Disabled
public class SensorOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        SensorArray sensorArray = new SensorArray();
        sensorArray.addSensor(Sensor.touchSensor(this.hardwareMap, "touch")).addSensor(Sensor.analogEncoder(AnalogEncoder.rev_potentiometer(hardwareMap, "potent")));
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
