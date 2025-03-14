package org.gentrifiedApps.gentrifiedAppsUtil.velocityVision;

//package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Alliance;
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.sample.ReturnType;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.sample.CameraLock;
import org.gentrifiedApps.gentrifiedAppsUtil.velocityVision.pipelines.sample.SampleDataDetector;
//@Autonomous
public class testAuto extends LinearOpMode {
    private SampleDataDetector processor;
    private VisionPortal portal;

    @Override
    public void runOpMode() {
        processor = new SampleDataDetector(
                ReturnType.all(),
                Alliance.RED
        );

        portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")) // set camera name
                .setCameraResolution(new Size(640, 480)) // set the camera resolution
                .addProcessor(processor) // add the processor we just created
                .build();
        waitForStart(); // wait for start as used in opmode
        CameraLock cameraLock = processor.returnCameraLock();
        cameraLock.getAngle();
        cameraLock.getColor();
        cameraLock.getCenter();
    }
}