# Odometer

The odometer is a way to track how far your robot has traveled.

To use it, either do `Odometer.newOdometer(driver)` or `Odometer.newOdometer(opMode, startPoint)`.

Then in your code, you can do `odometer.update()` to update the odometer. This will update the odometer with the current
position of the robot.

You also should use `odometer.telemetry(telemetry)` to show the data.