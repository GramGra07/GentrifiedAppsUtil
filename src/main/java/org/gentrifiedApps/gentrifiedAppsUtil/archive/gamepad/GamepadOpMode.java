//package org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad;
//
//
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//@TeleOp
//@Disabled
//public class GamepadOpMode extends LinearOpMode {
//
//    @Override
//    public void runOpMode() {
//        GamepadPlus gamepad = new GamepadPlus(this.gamepad1);
//        GamepadPair gamepadPair = new GamepadPair(this.gamepad1, this.gamepad2);
//        waitForStart();
//        while (opModeIsActive()) {
//            gamepadPair.getGamepad1Plus().buttonJustPressed(Button.LEFT_BUMPER); //little more tedious to do this
//            gamepadPair.getGamepad2Plus().buttonJustPressed(Button.LEFT_BUMPER);
//            gamepadPair.sync();// syncs both gamepads
//
//            gamepad.buttonJustPressed(Button.LEFT_BUMPER); //checks if button was just pressed
//            gamepad.sync(); // syncs gamepad
//        }
//    }
//}
