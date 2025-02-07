package org.gentrifiedApps.gentrifiedAppsUtil.looptime.analyzer;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp(name = "AnalysisOpMode", group = "Linear OpMode")
@Disabled
public class AnalysisOpMode extends LinearOpMode {
    private LoopTimeAnalyzer loopTimeAnalyzer;

    public AnalysisOpMode(Integer testingLoops, Runnable initLoop, List<TestableFunctions> funcs) {
        this.loopTimeAnalyzer = new LoopTimeAnalyzer(this, testingLoops, initLoop, funcs);
    }

    @Override
    public void runOpMode() {
        loopTimeAnalyzer.init();
        waitForStart();
        while (opModeIsActive()) {
            loopTimeAnalyzer.testEach();
        }
    }
}
