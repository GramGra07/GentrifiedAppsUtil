package org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.robot.classWrappers;

public abstract class LinearOpModeW {
    enum State {
        running, stopped, init
    }

    State s = State.stopped;

    protected final TelemetryW telemetry = new TelemetryW();
    protected final HWMapW hwMap = new HWMapW();

    public abstract void runOpMode();

    public boolean opModeIsActive() {
        return s == State.running;
    }

    public void requestOpModeStop() {
        s = State.stopped;
    }

    public boolean waitForStart() {
        s = State.init;
        final String prompt = "[waitForStart] ran and started";
        System.out.println(prompt);
        s = State.running;
        return true;
    }
}