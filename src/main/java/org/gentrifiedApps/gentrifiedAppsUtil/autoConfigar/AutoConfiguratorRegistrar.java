package org.gentrifiedApps.gentrifiedAppsUtil.autoConfigar;

import android.icu.lang.UScript;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.gentrifiedApps.gentrifiedAppsUtil.gamepad.Button;

import java.util.HashMap;
import java.util.List;

public final class AutoConfiguratorRegistrar {

    static Button passButton = Button.CROSS;

    static HashMap<String, ButtonConfig> buttonMap = new HashMap<>();

    static {
        buttonMap.put("positionalXgain", new ButtonConfig(Button.DPAD_UP,null));
        buttonMap.put("positionalYgain", new ButtonConfig(Button.DPAD_DOWN,null));
        buttonMap.put("turnGain", new ButtonConfig(Button.DPAD_LEFT,null));
    }
    static Runnable initFunc = () -> {
    };

    static List<PassCondition> passConditions = List.of(new PassCondition(()->{}, List.of("positionalXgain", "positionalYgain", "turnGain"),null,null));

    static List<funcStore> funcs = List.of(new funcStore(() -> {
        // Function implementation
        runAction = true
        runBlocking(
                SequentialAction(
                        robot.driverAid.daAction(listOf(Runnable { robot.driverAid.highBasket() })),

        ParallelAction(
                SequentialAction(
                        robot.drive.actionBuilder(
                                        lastPose
                                )
                                .setTangent(Math.toRadians(180.0))
                                .strafeToLinearHeading(
                                        Vector2d(redBasket.position.x, redBasket.position.y),
                                        Math.toRadians(45.0)
                                )
                                .build(),
                        robot.endAction()
                ),
                robot.uAction(
                        robot.driverAid,
                        robot.armSubsystem,
                        robot.scoringSubsystem,
                        1000.0
                ),
                ),
                )
            )
    }, () -> {
        runBlocking(
                SequentialAction(
                        InstantAction { runAction = true },
        ParallelAction(
                robot.uAction(
                        robot.driverAid,
                        robot.armSubsystem,
                        robot.scoringSubsystem,
                        450.0
                ),
                SequentialAction(
                        robot.scoringSubsystem.servoAction(
                                listOf(
                                        Runnable { robot.scoringSubsystem.setPitchHigh() },
                                )
                            ),

        SleepAction(0.2),

                robot.scoringSubsystem.servoAction(
                        listOf(
                                Runnable { robot.scoringSubsystem.openClaw() },
                                )
                            ),
        SleepAction(0.2),

                robot.scoringSubsystem.servoAction(
                        listOf(
                                Runnable { robot.scoringSubsystem.setPitchMed() },
                                )
                            ),
        InstantAction { runAction = false },
                        )
                    ),
                )
            )
    }));

    static Boolean enableOpMode = false;

    private AutoConfiguratorRegistrar() {
    }

    private static OpModeMeta metaForClass(Class<? extends OpMode> cls) {
        return new OpModeMeta.Builder()
                .setName(cls.getSimpleName())
                .setGroup("GentrifiedApps")
                .setFlavor(OpModeMeta.Flavor.TELEOP)
                .build();
    }

    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        if (!enableOpMode) {
            return;
        }
        manager.register(
                metaForClass(Usage.class), new Usage(passButton,buttonMap,initFunc,funcs,passConditions)
        );
    }

}