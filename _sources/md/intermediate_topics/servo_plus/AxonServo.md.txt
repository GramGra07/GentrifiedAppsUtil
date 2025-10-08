# AxonServo

## `AxonServo` Class

The `AxonServo` class represents a servo motor with an analog encoder for position feedback.

**Constructor Parameters:**
- `hw: HardwareMap` - The hardware map to initialize the servo and encoder.
- `name: String` - The name of the servo.

**Properties:**
- `encoder: AnalogInput` - The analog input representing the encoder.
- `servo: ServoPlus` - The servo motor.

**Methods:**
- `initAEncoder(HardwareMap hw): AnalogInput` - Initializes the analog encoder.
- `telemetry(Telemetry telemetry)` - Adds encoder position data to telemetry.
- `setPosition(double degree)` - Sets the position of the servo in degrees.
- `getEncoderPosition(): double` - Gets the current position of the encoder in degrees.
- `getEncoderPositionReversed(): double` - Gets the reversed position of the encoder in degrees.

### Usage Example

```java
public class ExampleOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        HardwareMap hardwareMap = hardwareMap;
        Telemetry telemetry = telemetry;

        // Initialize AxonServo
        AxonServo axonServo = new AxonServo(hardwareMap, "servoName");

        waitForStart();

        while (opModeIsActive()) {
            // Set servo position
            axonServo.setPosition(90.0);

            // Add telemetry data
            axonServo.telemetry(telemetry);
            telemetry.update();
        }
    }
}
```