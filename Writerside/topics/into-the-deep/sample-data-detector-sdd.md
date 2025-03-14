# Sample Data Detector (sdd)

Sample data detector does as it says, it gets information of the sample closest to the center and gives you information of the selected sample. This is mainly for the Into the Deep season as it is important to be able to see the orientation and color of the sample closest.

There aren't any builders for this class as it is much easier to use than moa or bow. There are two variables you enter into the class as follows.

```java
ReturnType.all()
//or 
listOf(ReturnType.COLOR, ReturnType.CENTER, ReturnType.ANGLE)
```

```java
Alliance.RED 
//or 
Alliance.BLUE
```

Complete code should look like this:

```java
package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.gentrifiedApps.velocityvision.pipelines.sample.ReturnType;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.gentrifiedApps.velocityvision.enums.Alliance;
import org.gentrifiedApps.velocityvision.pipelines.sample.CameraLock;
import org.gentrifiedApps.velocityvision.pipelines.sample.SampleDataDetector;
@Autonomous
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
        CameraLock cameraLock = processor.returnCameraLock(); // set camera lock to camera lock from processor
        cameraLock.getAngle(); // gets the angle from the camera lock
        cameraLock.getColor(); // gets color from the camera
        cameraLock.getCenter(); // gets center of object from camera
    }
}
```

This all makes the detector as well as showing how to get information from it.
