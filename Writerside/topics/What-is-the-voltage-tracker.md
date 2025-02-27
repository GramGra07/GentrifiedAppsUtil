# What is the voltage tracker?

Voltage tracker simply tracks the voltage and allows you to view it in a readable format.

It can be used synchronously with the voltage compensation library for less class reads.

# How to use it
To initialize, use the following code:

```java
VoltageTracker(hwMap);
```
Where hwMap is your hardware map.

Then, in your main loop, use the following code:

```java
voltageTracker.update();
// to get telemetry, use instead
voltageTracker.telemetry(telemetry);
```

If you want to use the voltage compensator with the tracker, you must add the parameters `true` and `kf` to the `VoltageTracker` object.

Then, you can use `calculateVoltageCompensatedKf(controlEffort)` to get the compensated kf. It returns the value after correction.