# How to use it?

First, make sure you setup the gamepad before your runOpMode as such&#x20;

```java
GamepadPlus gamepad = new GamepadPlus(this.gamepad1);
```

Then, just call the variable. function you want to use, it has many and all self explanatory.

Lastly, run below code at the end of while(opModeIsActive) in order to make it all work.

```java
gamepad.sync()
```

Make sure you use the correct enum when calling the button, the full example is below.

```java
@TeleOp
@Disabled
public class GamepadOpMode extends LinearOpMode {
    GamepadPlus gamepad = new GamepadPlus(this.gamepad1);
    GamepadPair gamepadPair = new GamepadPair(this.gamepad1,this.gamepad2); //contains both gamepads

    @Override
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive()) {
            gamepadPair.getGamepad1Plus().buttonJustPressed(Button.LEFT_BUMPER); //little more tedious to do this
            gamepadPair.getGamepad2Plus().buttonJustPressed(Button.LEFT_BUMPER);
            gamepadPair.sync();// syncs both gamepads

            gamepad.buttonJustPressed(Button.LEFT_BUMPER); //checks if button was just pressed
            gamepad.sync(); // syncs gamepad
        }
    }
}

```

