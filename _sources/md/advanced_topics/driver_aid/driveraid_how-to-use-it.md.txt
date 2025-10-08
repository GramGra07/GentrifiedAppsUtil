# How to use the DriverAid

The `DriverAid` class allows you to control the way your driver aid functions are run. You can add `DAFunc` functions to the class, then set the state in order to run it continuously.

## Step-by-Step Guide

1. **Define Your Enum States**: Create an enum to represent the different states of your driver aid. This should contain an **IDLE** state.

    ```java
    public enum DA {
        DRIVE,
        TURN,
        TEST,
        IDLE
    }
    ```

2. **Initialize the `DriverAid` Class**: Create an instance of the `DriverAid` class, passing your enum class as a parameter.

    ```java
    DriverAid<DA> driverAid = new DriverAid<>(DA.class);
    ```

3. **Create `DAFunc` Instances**: Define the functions that will run based on the state of the robot. Each `DAFunc` instance requires the `DriverAid` instance, the state, and the functions to run.

Essentially, it is the driver aid, then your state, then the **initializer** function (runs once at the beginning), the **constant** function (runs every loop), the **isEnded** function (returns true if the function is done), and the **reset** function (runs once at the beginning).

```java
DriverAid.DAFunc<DA> func1 = new DriverAid.DAFunc<>(
        driverAid,
        DA.DRIVE,
        () -> telemetry.addData("func1", "drive init"),
        () -> telemetry.addData("func1", "drive constant"),
        () -> false,
        () -> telemetry.addData("func1", "drive reset")
);

DriverAid.DAFunc<DA> func2 = new DriverAid.DAFunc<>(
        driverAid,
        DA.TURN,
        () -> telemetry.addData("func2", "turn init"),
        () -> telemetry.addData("func2", "turn constant"),
        () -> false,
        () -> telemetry.addData("func2", "turn reset")
);

DriverAid.DAFunc<DA> func3 = new DriverAid.DAFunc<>(
        driverAid,
        DA.TEST,
        () -> telemetry.addData("func3", "test init"),
        () -> telemetry.addData("func3", "test constant"),
        () -> false,
        () -> telemetry.addData("func3", "test reset")
);
// you do not need one for idle state since it is built automatically
```

4. **Initialize a Function**: Call `runInit()` on a `DAFunc` instance to set it as the current function and run its initialization code.

    ```java
    func1.runInit();
   //OR
    driverAid.setDriverAidFunction(func1);
   // or
   driverAid.idle(DA.IDLE); // for idle state
    ```

5. **Update the `DriverAid`**: In your main loop, call `update()` on the `DriverAid` instance to continuously run the current function.

    ```java
    while (opModeIsActive()) {
        driverAid.update();
        telemetry.update();
    }
    ```

6. **Switch Functions Based on Input**: Use gamepad (or GamepadPlus) input or other conditions to switch between different `DAFunc` instances.

    ```java
    if (gamepad1.dpad_right) {
        driverAid.setDriverAidFunction(func1); // options for this
    } else if (gamepad1.dpad_up) {
        func2.runInit();
    } else if (gamepad1.dpad_left) {
        func3.runInit();
    } else if (gamepad1.dpad_down) {
        driverAid.idle(DA.IDLE);
    }
    ```

### Full Example

```java
enum DA{
    DRIVE,
    TURN,
    TEST,
    IDLE
}
@TeleOp
public class GentrifiedAppsTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DriverAid<DA> driverAid = new DriverAid<>(DA.class);

        DriverAid.DAFunc<DA> func1 = new DriverAid.DAFunc<>(
                driverAid,
                DA.DRIVE,
                () -> telemetry.addData("func1", "drive init"),
                () -> telemetry.addData("func1", "drive constant"),
                () -> false, // unnecessary unless you want to use is ended
                () -> telemetry.addData("func1", "drive reset") // unnecessary unless you want to use reset
        );

        DriverAid.DAFunc<DA> func2 = new DriverAid.DAFunc<>(
                driverAid,
                DA.TURN,
                () -> telemetry.addData("func2", "turn init"),
                () -> telemetry.addData("func2", "turn constant"),
                () -> false,
                () -> telemetry.addData("func2", "turn reset")
        );

        DriverAid.DAFunc<DA> func3 = new DriverAid.DAFunc<>(
                driverAid,
                DA.TEST,
                () -> telemetry.addData("func3", "test init"),
                () -> telemetry.addData("func3", "test constant"),
                () -> false,
                () -> telemetry.addData("func3", "test reset")
        );

        func1.runInit();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_right) {
                driverAid.setDriverAidFunction(func1);
            } else if (gamepad1.dpad_up) {
                func2.runInit();
            } else if (gamepad1.dpad_left) {
                func3.runInit();
            } else if (gamepad1.dpad_down) {
                driverAid.idle(DA.IDLE);
            }
            driverAid.update();
            telemetry.update();
        }
    }
}
```