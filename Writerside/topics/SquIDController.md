# SquIDController

The `SquIDController` class is like a PID Controller but instead takes the square root.

To use it, you need to find the p value that gives you the best results.

You will use the `calculate` method to get the output of the controller.

## Example

```Java
@TeleOp
public class SquidTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SquIDController controller = new SquIDController(0.5);
        waitForStart();
        while (opModeIsActive()) {
            motor.setPower(controller.calculate(setpoint, currentValue));
        }
    }

}
```