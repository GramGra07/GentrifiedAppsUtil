//package org.gentrifiedApps.gentrifiedAppsUtil.autoConfigar
//
//package org.firstinspires.ftc.teamcode.opModes.teleOp
//
//import com.acmerobotics.roadrunner.InstantAction
//import com.acmerobotics.roadrunner.ParallelAction
//import com.acmerobotics.roadrunner.Pose2d
//import com.acmerobotics.roadrunner.SequentialAction
//import com.acmerobotics.roadrunner.SleepAction
//import com.acmerobotics.roadrunner.Vector2d
//import com.acmerobotics.roadrunner.ftc.runBlocking
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp
//import org.firstinspires.ftc.teamcode.customHardware.AutoHardware
//import org.firstinspires.ftc.teamcode.customHardware.AutoHardware.Companion.lastPose
//import org.firstinspires.ftc.teamcode.customHardware.AutoHardware.Companion.redBasket
//import org.firstinspires.ftc.teamcode.customHardware.AutoHardware.Companion.redStartLeft
//import org.firstinspires.ftc.teamcode.customHardware.AutoHardware.Companion.runAction
//import org.firstinspires.ftc.teamcode.customHardware.autoUtil.StartLocation
//import org.firstinspires.ftc.teamcode.customHardware.autoUtil.startEnums.Alliance
//import org.firstinspires.ftc.teamcode.extensions.GamepadExtensions
//import org.firstinspires.ftc.teamcode.extensions.GamepadExtensions.buttonJustPressed
//import org.firstinspires.ftc.teamcode.utilClass.storage.TempAutoDAFix
//import org.firstinspires.ftc.teamcode.utilClass.varConfigurations.AutoVars
//import org.firstinspires.ftc.teamcode.utilClass.varConfigurations.DAVars
//
//class AutoSampleMaxTuner : LinearOpMode() {
//    fun Pose2d.angleTo(pose2d: Pose2d): Double {
//        val deltaX = pose2d.position.x - this.position.x
//        val deltaY = pose2d.position.y - this.position.y
//        return Math.atan2(deltaY, deltaX)
//    }
//
//    var gain = 1
//    var lastGain = gain
//    fun gainControl() {
//        if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.RIGHT_BUMPER, 1)) {
//            gain++
//        } else if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.LEFT_BUMPER, 1)) {
//            gain--
//        }
//        if (lastGain != gain) {
//            lastGain = gain
//        }
//    }
//
//    fun whatGain() {
//        if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.TRIANGLE, 1)) {
//            positionalXgain += gain
//        } else if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.SQUARE, 1)) {
//            positionalXgain -= gain
//        }
////        if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.CIRCLE, 1)) {
////            positionalYgain += gain
////        } else if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.CROSS, 1)) {
////            positionalYgain -= gain
////        }
//        if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.DPAD_UP, 1)) {
//            extensionGain += gain
//        } else if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.DPAD_DOWN, 1)) {
//            extensionGain -= gain
//        }
//        if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.DPAD_LEFT, 1)) {
//            turnGain += gain
//        } else if (gamepad1.buttonJustPressed(GamepadExtensions.PushButtons.DPAD_RIGHT, 1)) {
//            turnGain -= gain
//        }
//    }
//
//    fun gainChanged(): Boolean {
//        return lastTurnGain != turnGain || lastXgain != positionalXgain || lastYgain != positionalYgain
//    }
//    fun extensionGainChanged(): Boolean {
//        return lastExtensionGain != extensionGain
//    }
//
//    fun passthrough(): Boolean {
//        return gamepad1.cross
//    }
//
//    var positionalXgain = 0.0
//    var positionalYgain = 0.0
//    var turnGain = 0.0
//    var extensionGain = 0.0
//    var lastXgain = positionalXgain
//    var lastYgain = positionalYgain
//    var lastTurnGain = turnGain
//    var lastExtensionGain = extensionGain
//    fun resetGain() {
//        positionalXgain = 0.0
//        positionalYgain = 0.0
//        turnGain = 0.0
//        extensionGain = 0.0
//        lastGain()
//    }
//    fun lastGain(){
//        lastXgain = positionalXgain
//        lastYgain = positionalYgain
//        lastTurnGain = turnGain
//        lastExtensionGain = extensionGain
//    }
//
//    var finalOffsets: HashMap<String, Double> = hashMapOf()
//    override fun runOpMode() {
//        val robot = AutoHardware(this, StartLocation(Alliance.RED, redStartLeft))
//        robot.once()
//        waitForStart()
//        if (opModeIsActive()) {
//            runAction = true
//            runBlocking(
//                SequentialAction(
//                    robot.driverAid.daAction(listOf(Runnable { robot.driverAid.highBasket() })),
//
//                    ParallelAction(
//                        SequentialAction(
//                            robot.drive.actionBuilder(
//                                lastPose
//                            )
//                                .setTangent(Math.toRadians(180.0))
//                                .strafeToLinearHeading(
//                                    Vector2d(redBasket.position.x, redBasket.position.y),
//                                    Math.toRadians(45.0)
//                                )
//                                .build(),
//                            robot.endAction()
//                        ),
//                        robot.uAction(
//                            robot.driverAid,
//                            robot.armSubsystem,
//                            robot.scoringSubsystem,
//                            1000.0
//                        ),
//                    ),
//                )
//            )
//            while (!gamepad1.cross) {
//                if (gamepad1.cross){
//                    break
//                }
//                telemetry.addData("Gain", gain)
//                telemetry.update()
//                if (gainChanged()) {
//                    runBlocking(
//                        ParallelAction(
//                            SequentialAction(
//                                robot.drive.actionBuilder(
//                                    lastPose
//                                )
//                                    .strafeToLinearHeading(
//                                        Vector2d(
//                                            redBasket.position.x + positionalXgain,
//                                            redBasket.position.y + positionalYgain
//                                        ),
//                                        Math.toRadians(45.0 + turnGain)
//                                    )
//                                    .build(),
//                            ),
//                            robot.uAction(
//                                robot.driverAid,
//                                robot.armSubsystem,
//                                robot.scoringSubsystem,
//                                1000.0
//                            ),
//                        ),
//                    )
//                    lastGain()
//                }
//                robot.updateAll()
//                whatGain()
//                gainControl()
//            }
//            runBlocking(
//                SequentialAction(
//                    InstantAction { runAction = true },
//                    ParallelAction(
//                        robot.uAction(
//                            robot.driverAid,
//                            robot.armSubsystem,
//                            robot.scoringSubsystem,
//                            450.0
//                        ),
//                        SequentialAction(
//                            robot.scoringSubsystem.servoAction(
//                                listOf(
//                                    Runnable { robot.scoringSubsystem.setPitchHigh() },
//                                )
//                            ),
//
//                            SleepAction(0.2),
//
//                            robot.scoringSubsystem.servoAction(
//                                listOf(
//                                    Runnable { robot.scoringSubsystem.openClaw() },
//                                )
//                            ),
//                            SleepAction(0.2),
//
//                            robot.scoringSubsystem.servoAction(
//                                listOf(
//                                    Runnable { robot.scoringSubsystem.setPitchMed() },
//                                )
//                            ),
//                            InstantAction { runAction = false },
//                        )
//                    ),
//                )
//            )
//            finalOffsets.put("positionalXgain", positionalXgain)
//            finalOffsets.put("positionalYgain", positionalYgain)
//            finalOffsets.put("turnGain", turnGain)
//            resetGain()
////            //////////
//            runAction = true
//            var pose = Pose2d(-48.0, -25.0, Math.toRadians(90.0))
//            var turnAngle = lastPose.angleTo(pose) + Math.toRadians(AutoVars.sampleRightTurnOffset)
//
//            var offset = AutoVars.sampleRightPositionOffset
////            robot.tempAuto.store(TempAutoDAFix.DATypes.PICKUP_E)
////            robot.tempAuto.set(TempAutoDAFix.DATypes.PICKUP_E, (robot.calculateExtend(
////                lastPose,
////                pose
////            ) + offset.toDouble()) * robot.armSubsystem.ticksPerInchExtend)
//            DAVars.pickUpE = (robot.calculateExtend(
//                lastPose,
//                pose
//            ) + offset.toDouble()) * robot.armSubsystem.ticksPerInchExtend
//            runBlocking(
//                SequentialAction(
//                    robot.driverAid.daAction(listOf(Runnable { robot.driverAid.pickup() })),
//                    ParallelAction(
//                        SequentialAction(
//                            robot.drive.actionBuilder(
//                                lastPose
//                            )
//                                .turnTo((turnAngle))
//                                .build(),
//                            robot.endAction(),
//                        ),
//                        robot.uAction(robot.driverAid, robot.armSubsystem, robot.scoringSubsystem,200.0),
//                    ),
//                    robot.scoringSubsystem.servoAction(
//                        listOf(
//                            Runnable { robot.scoringSubsystem.setPitchLow() },
//                        )
//                    ),
//                )
//            )
//            while (!gamepad1.cross) {
//                if (gamepad1.cross){
//                    break
//                }
//                robot.updateAll()
//                if (extensionGainChanged()) {
//                    robot.armSubsystem.extendTarget += ((extensionGain-lastExtensionGain) * robot.armSubsystem.ticksPerInchExtend).toInt()
//                    lastGain()
//                }
//                if (gainChanged()) {
//                    runBlocking(
//                        ParallelAction(
//                            SequentialAction(
//                                robot.drive.actionBuilder(
//                                    lastPose
//                                )
//                                    .turnTo((turnAngle + Math.toRadians(turnGain)))
//                                    .build(),
//                                robot.endAction()
//                            ),
//                        ),
//                    )
//                    lastGain()
//                }
//                whatGain()
//                gainControl()
//                telemetry.addData("Gain", gain)
//                telemetry.update()
//            }
//            robot.endAction()
//            runBlocking(
//                ParallelAction(
//                    SequentialAction(
//                        robot.scoringSubsystem.servoAction(
//                            listOf(
//                                Runnable { robot.scoringSubsystem.closeClaw() },
//                            )
//                        ),
//                        SleepAction(0.2),
//                        robot.driverAid.daAction(listOf(Runnable { robot.driverAid.collapse() })),
//                        ParallelAction(
//                            robot.scoringSubsystem.servoAction(
//                                listOf(
//                                    Runnable { robot.scoringSubsystem.setPitchMed() },
//                                )
//                            ),
//                            robot.uAction(
//                                robot.driverAid,
//                                robot.armSubsystem,
//                                robot.scoringSubsystem,
//                                250.0
//                            ),
//                        ),
//                        InstantAction {
//                            DAVars.pickUpE = 1800.0
////                            robot.tempAuto.resetVar(TempAutoDAFix.DATypes.PICKUP_E)
//                        },
//                    )
//                )
//            )
//            finalOffsets.put("extensionGainR", extensionGain)
//            finalOffsets.put("turnGainR", turnGain)
//            resetGain()
//
//
//            runBlocking(
//                SequentialAction(
//                    robot.drive.actionBuilder(
//                        lastPose).turnTo(Math.toRadians(45.0+turnGain)).build(),
//                    robot.endAction()
//                )
//            )
////
////            //////////
////
//            runAction = true
//            pose = Pose2d(-58.0, -25.0, Math.toRadians(90.0))
//            turnAngle = lastPose.angleTo(pose) + Math.toRadians(AutoVars.sampleCenterTurnOffset)
//
//            offset = AutoVars.sampleCenterPositionOffset
//
//            DAVars.pickUpE = (robot.calculateExtend(
//                lastPose,
//                pose
//            ) + offset.toDouble()) * robot.armSubsystem.ticksPerInchExtend
//            runBlocking(
//                SequentialAction(
//                    robot.driverAid.daAction(listOf(Runnable { robot.driverAid.pickup() })),
//                    ParallelAction(
//                        SequentialAction(
//                            robot.drive.actionBuilder(
//                                lastPose
//                            )
//                                .turnTo((turnAngle))
//                                .build(),
//                            robot.endAction(),
//                        ),
//                        robot.uAction(robot.driverAid, robot.armSubsystem, robot.scoringSubsystem),
//                    ),
//                    robot.scoringSubsystem.servoAction(
//                        listOf(
//                            Runnable { robot.scoringSubsystem.setPitchLow() },
//                        )
//                    ),
//
//                    robot.scoringSubsystem.servoAction(
//                        listOf(
//                            Runnable { robot.scoringSubsystem.setRotateCenter() },
//                        )
//                    ),
//                )
//            )
//            while (!gamepad1.cross) {
//                if (gamepad1.cross){
//                    break
//                }
//                robot.updateAll()
//                if (extensionGainChanged()) {
//                    robot.armSubsystem.extendTarget += ((extensionGain-lastExtensionGain) * robot.armSubsystem.ticksPerInchExtend).toInt()
//                    lastGain()
//                }
//                if (gainChanged()) {
//                    runBlocking(
//                        ParallelAction(
//                            SequentialAction(
//                                robot.drive.actionBuilder(
//                                    lastPose
//                                )
//                                    .turnTo((turnAngle + Math.toRadians(turnGain)))
//                                    .build(),
//                                robot.endAction(),
//                            ),
//                        ),
//                    )
//                    lastGain()
//                }
//                whatGain()
//                gainControl()
//                telemetry.addData("Gain", gain)
//                telemetry.update()
//            }
//            robot.endAction()
//            runBlocking(
//                ParallelAction(
//                    SequentialAction(
//                        robot.scoringSubsystem.servoAction(
//                            listOf(
//                                Runnable { robot.scoringSubsystem.closeClaw() },
//                            )
//                        ),
//                        SleepAction(0.2),
//                        robot.driverAid.daAction(listOf(Runnable { robot.driverAid.collapse() })),
//                        ParallelAction(
//                            robot.scoringSubsystem.servoAction(
//                                listOf(
//                                    Runnable { robot.scoringSubsystem.setPitchMed() },
//                                )
//                            ),
//                            robot.uAction(
//                                robot.driverAid,
//                                robot.armSubsystem,
//                                robot.scoringSubsystem,
//                                250.0
//                            ),
//                        ),
//                        InstantAction { DAVars.pickUpE = 1800.0 },
//                    )
//                )
//            )
//            finalOffsets.put("extensionGainC", extensionGain)
//            finalOffsets.put("turnGainC", turnGain)
//            resetGain()
//
//            runBlocking(
//                SequentialAction(
//                    robot.drive.actionBuilder(
//                        lastPose).turnTo(Math.toRadians(45.0+turnGain)).build(),
//                    robot.endAction()
//                )
//            )
////            //////////
//            runAction = true
//            pose = Pose2d(-68.0, -25.0, Math.toRadians(90.0))
//            turnAngle = lastPose.angleTo(pose) + Math.toRadians(AutoVars.sampleLeftTurnOffset)
//
//            offset = AutoVars.sampleLeftPositionOffset
//
//            DAVars.pickUpE = (robot.calculateExtend(
//                lastPose,
//                pose
//            ) + offset.toDouble()) * robot.armSubsystem.ticksPerInchExtend
//            runBlocking(
//                SequentialAction(
//                    robot.driverAid.daAction(listOf(Runnable { robot.driverAid.pickup() })),
//                    ParallelAction(
//                        SequentialAction(
//                            robot.drive.actionBuilder(
//                                lastPose
//                            )
//                                .turnTo((turnAngle))
//                                .build(),
//                            robot.endAction(),
//                        ),
//                        robot.uAction(robot.driverAid, robot.armSubsystem, robot.scoringSubsystem),
//                    ),
//                    robot.scoringSubsystem.servoAction(
//                        listOf(
//                            Runnable { robot.scoringSubsystem.setPitchLow() },
//                        )
//                    ),
//
//                    robot.scoringSubsystem.servoAction(
//                        listOf(
//                            Runnable { robot.scoringSubsystem.setRotateFreakBob() },
//                        )
//                    ),
//                )
//            )
//            while (!gamepad1.cross) {
//                if (gamepad1.cross){
//                    break
//                }
//                if (extensionGainChanged()) {
//                    robot.armSubsystem.extendTarget += ((extensionGain-lastExtensionGain) * robot.armSubsystem.ticksPerInchExtend).toInt()
//                    lastGain()
//                }
//                if (gainChanged()) {
//                    runBlocking(
//                        ParallelAction(
//                            SequentialAction(
//                                robot.driverAid.daAction(listOf(Runnable { robot.driverAid.pickup() })),
//                                robot.drive.actionBuilder(
//                                    lastPose
//                                )
//                                    .turnTo((turnAngle + Math.toRadians(turnGain)))
//                                    .build(),
//                                robot.endAction(),
//                            ),
//                        ),
//                    )
//                    lastGain()
//                }
//                robot.updateAll()
//                whatGain()
//                gainControl()
//                telemetry.addData("Gain", gain)
//                telemetry.update()
//            }
//            robot.endAction()
//            runBlocking(
//                ParallelAction(
//                    SequentialAction(
//                        robot.scoringSubsystem.servoAction(
//                            listOf(
//                                Runnable { robot.scoringSubsystem.closeClaw() },
//                            )
//                        ),
//                        SleepAction(0.2),
//                        robot.driverAid.daAction(listOf(Runnable { robot.driverAid.collapse() })),
//                        ParallelAction(
//                            robot.scoringSubsystem.servoAction(
//                                listOf(
//                                    Runnable { robot.scoringSubsystem.setPitchMed() },
//                                )
//                            ),
//                            robot.uAction(
//                                robot.driverAid,
//                                robot.armSubsystem,
//                                robot.scoringSubsystem,
//                                250.0
//                            ),
//                        ),
//                        InstantAction { DAVars.pickUpE = 1800.0 },
//                    )
//                )
//            )
//            finalOffsets.put("extensionGainL", extensionGain)
//            finalOffsets.put("turnGainL", turnGain)
//            resetGain()
////
//            runBlocking(
//                SequentialAction(
//                    robot.drive.actionBuilder(
//                        lastPose).turnTo(Math.toRadians(45.0+turnGain)).build(),
//                    robot.endAction()
//                )
//            )
//            while (opModeIsActive() && !isStopRequested) {
//                finalOffsets.forEach() {
//                    telemetry.addData(it.key, it.value)
//                }
//                telemetry.update()
//            }
////
//
//        }
//    }
//
//}