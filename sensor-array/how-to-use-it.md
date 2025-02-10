# How to use it

The problem with FTC loop times is sensors, reading takes forever and some more than others. SensorArray takes care of that problem and allows you to auto read things.

```java
public class SensorOpMode extends LinearOpMode {
    SensorArray sensorArray = new SensorArray();

    @Override
    public void runOpMode() {
        sensorArray.addSensor(new Sensor("sensor1",SensorType.DIST,()-> hardwareMap.get(DistanceSensor.class, "sensor1"),1));
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
```

readLoopSaving allows it to save the current state of that device, then reads it with the all telemetry. You can also read from the storage. All of the individual getters use name as a param which is included in Sensor. Auto Loop runs the sensors every set amount of loops when you add a sensor.

SensorTypes such as DIST, COLOR, ENC, TOUCH, allow you to read it directly. If you use read loop saving, it must be done at the beginning.
