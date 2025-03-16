# How to use it

The problem with FTC loop times is sensors, reading takes forever and some more than others. SensorArray takes care of that problem and allows you to auto read things.

#### `SensorArray` Class

The `SensorArray` class manages a collection of sensors and provides methods to read and display their values.

**Methods:**
- `addSensor(sensor: Sensor): SensorArray` - Adds a sensor to the array and initializes it.
- `readAllLoopSaving()` - Reads all sensors and saves their values.
- `allTelemetry(telemetry: Telemetry)` - Displays telemetry for all sensors.
- `telemetry(telemetry: Telemetry, name: String)` - Displays telemetry for a specific sensor.
- `read(name: String, type: SensorData): Any` - Reads a specific sensor and returns its value.
- `autoLoop(loops: Int)` - Reads sensors at their specified periods.

#### `Sensor` Class

The `Sensor` class represents a sensor in the system. It includes initialization, reading, and periodic reading functionalities.

**Constructor Parameters:**
- `init: Runnable` - The initialization logic for the sensor.
- `sensorData: SensorData` - The data structure holding sensor metadata.
- `read: () -> Any` - The function to read the sensor value.
- `period: Int` - The period to read the sensor.

**Methods:**
- `initialize()` - Initializes the sensor.
- `lastRead(): Any` - Returns the last read value of the sensor.
- `read(): Any` - Reads the sensor value and updates `lastRead`.
- `readLoopSaving()` - Reads the sensor value and saves it to `lastRead`.

**Companion Object Methods:**
- `distanceSensor(hw: HardwareMap, name: String): Sensor` - Creates a `DistanceSensor`.
- `analogEncoder(hw: HardwareMap, name: String, operations: List<Operation>): Sensor` - Creates an `AnalogEncoder`.
- `analogEncoder(analogEncoder: AnalogEncoder): Sensor` - Creates a `Sensor` from an existing `AnalogEncoder`.
- `colorSensor(hw: HardwareMap, name: String): Sensor` - Creates a `ColorSensor`.
- `touchSensor(hw: HardwareMap, name: String): Sensor` - Creates a `TouchSensor`.
- `encoder(hw: HardwareMap, name: String): Sensor` - Creates an `Encoder`.
- `beamBreak(hw: HardwareMap, name: String): Sensor` - Creates a `BeamBreakSensor`.

#### `SensorData` Class

The `SensorData` class holds metadata about a sensor.

**Constructor Parameters:**
- `returnType: Class<out Any>` - The type of data the sensor returns.
- `name: String` - The name of the sensor.

**Companion Object Methods:**
- `distanceSensor(name: String): SensorData` - Creates `SensorData` for a `DistanceSensor`.
- `analogEncoder(name: String): SensorData` - Creates `SensorData` for an `AnalogEncoder`.
- `colorSensor(name: String): SensorData` - Creates `SensorData` for a `ColorSensor`.
- `touchSensor(name: String): SensorData` - Creates `SensorData` for a `TouchSensor`.
- `encoder(name: String): SensorData` - Creates `SensorData` for an `Encoder`.
- `beamBreak(name: String): SensorData` - Creates `SensorData` for a `BeamBreakSensor`.

### Usage Example

```kotlin
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.gentrifiedApps.gentrifiedAppsUtil.sensorArray.Sensor
import org.gentrifiedApps.gentrifiedAppsUtil.sensorArray.SensorArray

class ExampleOpMode : LinearOpMode() {
    override fun runOpMode() {
        val sensorArray = SensorArray()
        val hardwareMap: HardwareMap = hardwareMap
        val telemetry: Telemetry = telemetry

        // Add sensors to the array
        sensorArray.addSensor(Sensor.distanceSensor(hardwareMap, "distanceSensor"))
        sensorArray.addSensor(Sensor.touchSensor(hardwareMap, "touchSensor"))

        waitForStart()

        while (opModeIsActive()) {
            // Read all sensors and save their values
            sensorArray.readAllLoopSaving()

            // Display telemetry for all sensors
            sensorArray.allTelemetry(telemetry)
            telemetry.update()
        }
    }
}
```
