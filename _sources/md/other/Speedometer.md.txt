# Speedometer

The speedometer is a way to track how fast your robot is going at any given time.

To use it, either do `Speedometer.newSpeedometer(driver, ticksPerInch)` or
`Speedometer.newSpeedometer(opMode, startPoint)`.

Then in your code, you can do `speedometer.update()` to update the speedometer. This will update the speedometer with
the current position of the robot.

You also should use `speedometer.telemetry(telemetry)` to show the data.