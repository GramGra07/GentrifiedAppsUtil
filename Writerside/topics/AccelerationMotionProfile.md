# AccelerationMotionProfile

The `AccelerationMotionProfile` class generates an acceleration-only velocity profile. This class is useful for creating smooth motion profiles for robotic applications.

## Constructor

To initialize an `AccelerationMotionProfile`, you need to provide the following parameters:
- `maxVel`: The maximum velocity.
- `maxAccel`: The maximum acceleration.

```kotlin
AccelerationMotionProfile(maxVel: Double, maxAccel: Double)
```

## Methods

### `regenerateProfile(distance: Double)`

Regenerates the velocity profile for a given distance.

### `reset()`

Resets the profile, start time, and total time.

### `generateProfile(distance: Double)`

Generates the velocity profile for a given distance. This method calculates the acceleration time and creates a profile with time, velocity, and acceleration.

### `start()`

Starts the profile by setting the start time to the current time.

### `getTarget(): Pair<Double, Double>`

Returns the target velocity and acceleration at the current time.

### `getVelocity(): Double`

Returns the current target velocity.

## Example

```kotlin
val profile = AccelerationMotionProfile(2.0, 1.0)
profile.regenerateProfile(10.0)
profile.start()

val (velocity, acceleration) = profile.getTarget()
println("Velocity: $velocity, Acceleration: $acceleration")
```
```java
@TeleOp
public class TestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        AccelerationMotionProfile profile = new AccelerationMotionProfile(2.0, 1.0);
        profile.regenerateProfile(10.0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            motor.setPower(profile.getVelocity()); // this will automatically calculate it and do it once
            profile.reset();// this will reset the profile
            profile.regenerateProfile(10.0); // this will regenerate the profile and reset it all 
        }
    }
}
```