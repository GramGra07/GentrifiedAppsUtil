package org.gentrifiedApps.gentrifiedAppsUtil.looptime.analyzer;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

public class AnalysisOpMode extends LinearOpMode {
    private final LoopTimeAnalyzer loopTimeAnalyzer;

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
