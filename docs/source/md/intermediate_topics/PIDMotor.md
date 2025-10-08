# PIDMotor

PIDMotor is a class that automatically implements a PIDF controller into a DcMotor. This is beneficial to condense code and make it easier to use. The PIDMotor class is a subclass of the DcMotor class, which means it inherits all the properties and methods of the DcMotor class.

## How to use PIDMotor

To initialize a PIDMotor, you need to provide the following parameters:
- `hardwareMap`: The hardware map of the robot.
- `motorName`: The name of the motor in the hardware map.
- `p`: The proportional gain of the PID controller.
- `i`: The integral gain of the PID controller.
- `d`: The derivative gain of the PID controller.
- `f`: The feedforward gain of the PID controller.

Then, you can use the following methods to control the motor:

- `setTarget(int target)`: Sets the target position for the motor.
- `setPIDPower()`: Updates the PID controller and sets the motor power.
- `reset()`: Resets the PID controller.
- `getCurrentPosition()`: Returns the current position of the motor.
- `getTarget()`: Returns the target position of the motor.

- `setPIDF(double p, double i, double d, double f)`: Sets the PIDF values for the motor.

## Example

```Java
@TeleOp
public class GentrifiedAppsTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the motor and PID controller
        PIDMotor pidMotor = new PIDMotor(hardwareMap, "motor", 0.0001, 0.0, 0.0, 0.0);
        pidMotor.reset();

        waitForStart();

        // Set the target position for the PID controller
        pidMotor.setTarget(1000);

        while (opModeIsActive()) {
            // Update the PID controller and set the motor power
            pidMotor.setPIDPower();

            // Display the current position on telemetry
            telemetry.addData("PIDMotor Position", pidMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
```