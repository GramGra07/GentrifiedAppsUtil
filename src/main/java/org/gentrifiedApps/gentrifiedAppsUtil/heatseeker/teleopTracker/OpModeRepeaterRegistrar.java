package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.teleopTracker;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver;


public final class OpModeRepeaterRegistrar {

    static String name = "Test1";
    static Driver driver = new Driver("fl", "fr", "bl", "br", DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE);

    static boolean isEnabled = false;

    private OpModeRepeaterRegistrar()
    {
    }

    private static OpModeMeta metaForClass(Class<? extends OpMode> cls, OpModeMeta.Flavor flavor)
    {
        return new OpModeMeta . Builder ()
            .setName(cls.getSimpleName())
            .setGroup("Repeater")
            .setFlavor(flavor)
            .build();
    }

    @OpModeRegistrar
    public static void register(OpModeManager manager)
    {
        if (!isEnabled) return;
        manager.register(metaForClass(TeleOpTrackerOpMode.class, OpModeMeta.Flavor.AUTONOMOUS), new TeleOpCopyRunner (name,driver));

        manager.register(metaForClass(TeleOpTrackerOpMode.class, OpModeMeta.Flavor.TELEOP), new TeleOpTrackerOpMode (name,driver));
    }
}