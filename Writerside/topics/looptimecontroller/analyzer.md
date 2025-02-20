# Analyzer

This is a package to analyze loops by running the functions one at a time and timing them.

You can create it by putting this file anywhere in your code, it will automatically register the opMode once you fill out the initLoop and funcs.

Init loop will run to initialize things like servos and motors, testing functions have the name, then the function which allows you to test everything as shown below.

```java
package org.gentrifiedApps.gentrifiedAppsUtil.looptime.analyzer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import java.util.List;

public final class AnalyzerOpModeRegistrar {
    static Integer testingLoops = 5; 

    static Runnable initLoop = () -> {
    };
    static Boolean enableOpMode = true; // enables the opmode

    static List<TestableFunctions> funcs = List.of(
        new TestableFunctions("FunctionName", () -> {
            // Function implementation
        }),
    );

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
```

