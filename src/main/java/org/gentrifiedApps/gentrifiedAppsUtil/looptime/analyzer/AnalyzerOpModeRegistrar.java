package org.gentrifiedApps.gentrifiedAppsUtil.looptime.analyzer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import java.util.List;

public final class AnalyzerOpModeRegistrar {
    static Integer testingLoops = 200;

    static Runnable initLoop = () -> {
    };
    static Boolean enableOpMode = true;

    static List<TestableFunctions> funcs = List.of(new TestableFunctions("FunctionName", () -> {
        // Function implementation
    }));
    //new TestableFunctions("FunctionName", () -> {
    //    // Function implementation
    //})

    private AnalyzerOpModeRegistrar() {
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
                metaForClass(AnalysisOpMode.class), new AnalysisOpMode(testingLoops, initLoop, funcs)
        );
    }

}