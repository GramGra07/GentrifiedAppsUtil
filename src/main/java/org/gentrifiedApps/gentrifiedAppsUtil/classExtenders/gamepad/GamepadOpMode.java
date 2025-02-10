package org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.gamepad;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
@Disabled
public class GamepadOpMode extends LinearOpMode {
    GamepadPlus gamepad = new GamepadPlus(this.gamepad1, true);
    GamepadPair gamepadPair = new GamepadPair(this.gamepad1, this.gamepad2, true);

    @Override
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive()) {
            gamepadPair.loopSavingRead(); // only works when loopSaver is enabled // does both gamepads
            gamepadPair.getGamepad1Plus().buttonJustPressed(Button.LEFT_BUMPER); //little more tedious to do this
            gamepadPair.getGamepad2Plus().buttonJustPressed(Button.LEFT_BUMPER);
            gamepadPair.sync();// syncs both gamepads

            gamepad.loopSavingRead(); // only works when loopSaver is enabled
            gamepad.buttonJustPressed(Button.LEFT_BUMPER); //checks if button was just pressed
            gamepad.sync(); // syncs gamepad
        }
    }
}
