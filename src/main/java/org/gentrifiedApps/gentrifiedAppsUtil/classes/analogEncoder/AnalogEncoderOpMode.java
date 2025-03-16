package org.gentrifiedApps.gentrifiedAppsUtil.classes.analogEncoder;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Arrays;
import java.util.List;

@TeleOp
@Disabled
public class AnalogEncoderOpMode extends LinearOpMode {
    AnalogEncoder analogEncoder = null;
    List<Operation> operations = Arrays.asList(new Operation(Operand.DIVIDE, 3.0), new Operation(Operand.MULTIPLY, 2.0)); // operations to be performed on the encoder value

    @Override
    public void runOpMode() {
        analogEncoder = new AnalogEncoder(hardwareMap, "encoder", 0.0, operations);
        waitForStart();
        while (opModeIsActive()) {
            double currentPose = analogEncoder.getCurrentPosition(); // get the current position of the encoder
        }
    }
}
