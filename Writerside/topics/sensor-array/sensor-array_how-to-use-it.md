# How to use it

The `SensorArray` class represents an array of sensors and provides methods to read and manage them.

## Methods

### `addSensor(sensor: Sensor): SensorArray`

Adds a sensor to the array.

- **Parameters:**
    - `sensor`: The sensor to add.
- **Returns:** The updated `SensorArray` instance.

### `readAllLoopSaving()`

Reads all sensors. Should be used every loop at the beginning.

### `allTelemetry(telemetry: Telemetry)`

Provides telemetry for all sensors.

- **Parameters:**
    - `telemetry`: The telemetry to use.

### `telemetry(telemetry: Telemetry, name: String)`

Provides telemetry for an individual sensor.

- **Parameters:**
    - `telemetry`: The telemetry to use.
    - `name`: The name of the sensor.

### `read(name: String, type: SensorData): Any`

Reads an individual sensor.

- **Parameters:**
    - `name`: The name of the sensor.
    - `type`: The type of sensor data.
- **Returns:** The sensor data.

### `autoLoop(loops: Int)`

Reads all sensors at its periodic interval.

- **Parameters:**
    - `loops`: The number of loops, `loopTimeController.loops`.

### `autoLoop(ltc: LoopTimeController)`

Reads all sensors at its periodic interval.

- **Parameters:**
    - `ltc`: The `LoopTimeController` to use.
```

## See Also

- `Sensor`
- `SensorData`
- `LoopTimeController`
```

## Example

```java
package org.firstinspires.ftc.teamcode.ggutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.gentrifiedApps.gentrifiedAppsUtil.sensorArray.Sensor;
import org.gentrifiedApps.gentrifiedAppsUtil.sensorArray.SensorArray;

@TeleOp
public class GentrifiedAppsTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize sensors
        AnalogEncoder aEncoder = new AnalogEncoder(hardwareMap, "potent", 0.0, List.of(new Operation(Operand.MULTIPLY, 81.8)));

        SensorArray sensorArray = new SensorArray()
                .addSensor(Sensor.touchSensor(hardwareMap, "touch"))
                .addSensor(Sensor.colorSensor(hardwareMap,"color"))
                .addSensor(Sensor.analogEncoder(aEncoder));

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Read all sensors in the array
            sensorArray.readAllLoopSaving();

            // Display sensor values on telemetry
            sensorArray.allTelemetry(telemetry);

            telemetry.update();
        }
    }
}
```