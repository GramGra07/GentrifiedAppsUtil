package org.gentrifiedApps.gentrifiedAppsUtil.controllers.kinematics

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe
import org.gentrifiedApps.gentrifiedAppsUtil.classes.analogEncoder.AnalogEncoder
import org.gentrifiedApps.gentrifiedAppsUtil.classes.equations.SlopeIntercept
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.Button
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.gamepad.GamepadPlus
import org.gentrifiedApps.gentrifiedAppsUtil.hardware.motor.MotorExtensions.Companion.resetMotor

class AutoLevel1DfTuner @JvmOverloads constructor(
    val outputServoName: String,
    val inputEncoderName: String?,
    val potentiometerName: String?,
    var convertFactor: Double = 1.0,
) : LinearOpMode() {
    var state = States.NEEDS_CONVERT

    enum class States {
        NEEDS_CONVERT,
        CONVERTED,
        USING_ENCODER,
        USING_POTENTIOMETER,
        SLOPE_TUNING;

        fun setup(usingPotent: Boolean, usingEncoder: Boolean) {
            if (usingPotent && !usingEncoder) {
                this == USING_POTENTIOMETER
            } else if (!usingPotent && usingEncoder) {
                this == USING_ENCODER
            } else {
                throw IllegalArgumentException("You must provide either an encoder or a potentiometer name to use this tuner and not both.")
            }
        }
    }

    fun usingPotent(): Boolean {
        return potentiometerName != null && potentiometerName.isNotEmpty()
    }

    fun usingEncoder(): Boolean {
        return inputEncoderName != null && inputEncoderName.isNotEmpty()
    }

    init {
        Scribe.instance.startLogger(this)
        require(usingPotent() || usingEncoder()) {
            "You must provide either an encoder or a potentiometer name to use this tuner and not both."
        }
        require(usingPotent() and !usingEncoder() || !usingPotent() && usingEncoder()) {
            "You must provide either an encoder or a potentiometer name to use this tuner and not both."
        }
        if (convertFactor == 1.0 || convertFactor == 0.0) {
            state = States.NEEDS_CONVERT
        } else {
            state = States.CONVERTED
            state.setup(usingPotent(), usingEncoder())
        }
    }

    override fun runOpMode() {
        var startPotent = 0.0
        val output = hardwareMap.get(Servo::class.java, outputServoName)
        val input = if (usingEncoder()) {
            hardwareMap.get(com.qualcomm.robotcore.hardware.DcMotor::class.java, inputEncoderName!!)
        } else {
            AnalogEncoder.rev_potentiometer(hardwareMap, potentiometerName!!)
        }
        if (usingEncoder()) {
            input as com.qualcomm.robotcore.hardware.DcMotor
            resetMotor(input)
        }
        fun getInputValue(): Double {
            return if (usingEncoder()) {
                input as com.qualcomm.robotcore.hardware.DcMotor
                input.currentPosition.toDouble()
            } else {
                input as AnalogEncoder
                input.getCurrentPosition().toDouble() - startPotent
            }
        }

        fun calculateConvert(): Double {
            convertFactor = 90 / getInputValue()
            return convertFactor
        }

        val gamepad = GamepadPlus(gamepad1)
        waitForStart()
        var half = Point(0.0, 0.0)
        var servoPose = 0
        var offset = 0.0
        while (opModeIsActive()) {
            when (state) {
                States.NEEDS_CONVERT -> {
                    telemetry.addLine("State: NEEDS_CONVERT")
                    telemetry.addLine("If using a potentiometer, press gamepad1.a (CROSS) to get the initial position.")
                    if (gamepad.buttonJustPressed(Button.A) && usingPotent()) {
                        startPotent = getInputValue()
                    }
                    telemetry.addLine("If all done, move the lift to 90 degrees and press gamepad1.b (CIRCLE) to calculate the conversion factor.")
                    if (gamepad.buttonJustPressed(Button.B)) {
                        convertFactor = calculateConvert()
                    }
                    telemetry.update()
                    gamepad.sync()
                }

                States.CONVERTED -> {
                    telemetry.addLine("State: CONVERTED")
                    telemetry.addData("Conversion Factor", convertFactor)
                    telemetry.addLine("Make sure this looks right, then move the lift to 45 degrees and press gamepad1.x (SQUARE) to finish the test.")
                    Scribe.instance.setSet("1DfTuner").logData("Conversion Factor $convertFactor")
                    if (gamepad.buttonJustPressed(Button.X)) {
                        state.setup(usingPotent(), usingEncoder())
                    }
                    telemetry.update()
                    gamepad.sync()
                }

                States.USING_ENCODER,
                States.USING_POTENTIOMETER -> {
                    telemetry.addLine("Now press gamepad1.up or down to move the servo and tune the offset.")
                    if (gamepad.buttonJustPressed(Button.DPAD_UP)) {
                        servoPose++
                    } else if (gamepad.buttonJustPressed(Button.DPAD_DOWN)) {
                        servoPose--
                    }
                    telemetry.addData("Servo Position", output.position)
                    output.position = servoPose.toDouble()
                    telemetry.addData("OffsetInitial", output.position - (90 - getInputValue()))
                    offset = output.position - (90 - getInputValue())
                    telemetry.addLine("Press gamepad1.b (CIRCLE) to start the slope tuning process.")
                    if (gamepad.buttonJustPressed(Button.B)) {
                        half = Point(45.0, output.position - offset)
                        Scribe.instance.setSet("1DfTuner").logData("Offset Initial: $offset")
                        state = States.SLOPE_TUNING
                    }
                    telemetry.update()
                    gamepad.sync()
                }

                States.SLOPE_TUNING -> {
                    var servoSlope =
                        SlopeIntercept.fromPoints(half.x, half.y, 0, output.position - offset)
                    telemetry.addLine("Put the lift at 0 degrees")
                    telemetry.addLine("Press gamepad1.left or right to adjust the servo.")
                    if (gamepad.buttonJustPressed(Button.DPAD_LEFT)) {
                        servoPose -= 1
                    } else if (gamepad.buttonJustPressed(Button.DPAD_RIGHT)) {
                        servoPose += 1
                    }
                    output.position = servoPose.toDouble()
                    telemetry.addData("Slope", servoSlope)
                    telemetry.addLine("Press gamepad1.b (CIRCLE) to finish tuning.")
                    if (gamepad.buttonJustPressed(Button.B)) {
                        telemetry.addLine("Slope Tuning Finished")
                        telemetry.addData("Final Slope", servoSlope)
                        telemetry.addData("Final Offset", offset)
                        Scribe.instance.setSet("1DfTuner").logData("Final Slope: $servoSlope")
                    }
                    telemetry.update()
                    gamepad.sync()
                }
            }
        }
    }


}