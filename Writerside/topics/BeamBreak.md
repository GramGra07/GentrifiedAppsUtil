# BeamBreak

### Documentation for `BeamBreakSensor`

#### `BeamBreakSensor` Class

The `BeamBreakSensor` class represents a digital beam break sensor.

**Constructor Parameters:**
- `hw: HardwareMap` - The hardware map to initialize the sensor.
- `name: String` - The name of the sensor.

**Properties:**
- `beamBreak: DigitalChannel` - The digital channel representing the beam break sensor.

**Methods:**
- `isBroken(): Boolean` - Returns `true` if the beam is broken, `false` otherwise.
- `telemetry(Telemetry telemetry)` - Adds the beam break sensor state to telemetry.

### Usage Example

```java
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.sensors.BeamBreakSensor;

public class ExampleOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        HardwareMap hardwareMap = hardwareMap;
        Telemetry telemetry = telemetry;

        // Initialize BeamBreakSensor
        BeamBreakSensor beamBreakSensor = new BeamBreakSensor(hardwareMap, "beamBreakSensor");

        waitForStart();

        while (opModeIsActive()) {
            // Add telemetry data
            beamBreakSensor.isBroken();
            beamBreakSensor.telemetry(telemetry);
            telemetry.update();
        }
    }
}