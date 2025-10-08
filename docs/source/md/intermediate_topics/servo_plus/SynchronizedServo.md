# SynchronizedServo

## `SynchronizedServo` Class

The `SynchronizedServo` class represents a pair of synchronized servos, which can be either regular servos or Axon servos.

On setup, you MUST start it at 90 degrees, or halfway through the servo rotation.

**Constructor Parameters:**
- `hw: HardwareMap` - The hardware map to initialize the servos.
- `name: String` - The base name of the servos.
- `axonServo: Boolean` - A flag indicating whether the servos are Axon servos.

**Properties:**
- `servo1: ServoPlus` - The first regular servo (if `axonServo` is false).
- `servo2: ServoPlus` - The second regular servo (if `axonServo` is false).
- `servo1Axon: AxonServo` - The first Axon servo (if `axonServo` is true).
- `servo2Axon: AxonServo` - The second Axon servo (if `axonServo` is true).

**Methods:**
- `setPose(double pose)` - Sets the pose of the synchronized servos. If `axonServo` is true, it sets the position of the Axon servos; otherwise, it sets the position of the regular servos.

### Usage Example

```java
public class ExampleOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        HardwareMap hardwareMap = hardwareMap;
        Telemetry telemetry = telemetry;

        // Initialize SynchronizedServo
        SynchronizedServo synchronizedServo = new SynchronizedServo(hardwareMap, "servoName", true);

        waitForStart();

        while (opModeIsActive()) {
            // Set servo pose
            synchronizedServo.setPose(45.0);

            // Add telemetry data
            telemetry.addData("Servo Pose", "45 degrees");
            telemetry.update();
        }
    }
}
```