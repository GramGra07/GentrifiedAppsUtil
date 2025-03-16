package org.gentrifiedApps.gentrifiedAppsUtil.stateMachine.examples.abstracted;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.gentrifiedApps.gentrifiedAppsUtil.stateMachine.StateMachine;


@Autonomous
@Disabled
public class StateMachineAbstracted extends LinearOpMode {

    @Override
    public void runOpMode() {
        StateMachine<state> machine = abstractedSM.machine();
        waitForStart();
        machine.start();
        while (machine.mainLoop(this)) {
            machine.update();
        }
    }

    public enum state {
        INIT,
        StateOne,
        END,
        STOP,
    }
}