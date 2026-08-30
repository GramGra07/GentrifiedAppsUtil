package org.gentrifiedApps.gentrifiedAppsUtil.pidtuners.Opmodes;


import com.dacodingbeast.pidtuners.HardwareSetup.PivotSpecs;
import com.dacodingbeast.pidtuners.HardwareSetup.SlideSpecs;
import com.dacodingbeast.pidtuners.Opmodes.PIDCalculator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;


class PIDTuningOpModes {

    public static Double TS = 0.5;
    public static Double OS = 0.5;
    public static SlideSpecs s_specs = new SlideSpecs(6000, 105, 1, 28, 1.0, 1.0, (1 / 20), 400, 0);
    public static PivotSpecs p_specs = new PivotSpecs(6000, 105, 28, 1.0, 1.0, 1.0, (1 / 20));
    public static Boolean ENABLED = false;

    private static OpModeMeta metaForClass(Class<? extends OpMode> cls) {
        return new OpModeMeta.Builder()
                .setName(cls.getSimpleName())
                .setGroup("PIDTuners")
                .setFlavor(OpModeMeta.Flavor.TELEOP)
                .build();
    }

    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        if (!ENABLED) return;
        manager.register(metaForClass(PIDCalculator.class), new PIDCalculator(p_specs, s_specs, OS, TS));
    }
}


