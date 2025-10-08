# PIDFController

To initialize the PIDFController, you need to provide the following parameters:
- `kP`: Proportional gain
- `kI`: Integral gain
- `kD`: Derivative gain
- `kF`: Feedforward gain

You can change this at any time after the controller is initialized.

`PIDFController(kP, kI, kD, kF)`

`pidController.calculate(targetPosition, currentPosition)`

## Example

```java
@TeleOp
public class TestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        PIDFController pidController = new PIDFController(0.1, 0.01, 0.1, 0.0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            motor.setPower(pidController.calculate(targetPosition, currentPosition));
        }
    }
}
```