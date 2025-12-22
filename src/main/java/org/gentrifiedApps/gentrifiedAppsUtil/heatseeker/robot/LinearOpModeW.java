package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot;

import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.HWMapW;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers.TelemetryW;

public abstract class LinearOpModeW {
    enum State {
        running, stopped, init
    }

    long startTime = System.currentTimeMillis();
    State s = State.stopped;

    protected final TelemetryW telemetry = new TelemetryW();
    protected final HWMapW hwMap = new HWMapW();

    public abstract void runOpMode();

    public boolean opModeIsActive(Double timeout) {
        if (System.currentTimeMillis() - startTime >= timeout * 1000) {
            s = State.stopped;
            OutputFormatter.Companion.getInstance().sendData("opModeIsActive", "timeout");
        }
        return s == State.running;
    }

    public void requestOpModeStop() {
        s = State.stopped;
    }

    public boolean waitForStart() {
        s = State.init;
        final String prompt = "ran and started";
        OutputFormatter.Companion.getInstance().sendData("waitForStart", prompt);
        s = State.running;
        return true;
    }
}