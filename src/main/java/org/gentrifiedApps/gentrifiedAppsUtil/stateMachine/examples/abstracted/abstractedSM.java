package org.gentrifiedApps.gentrifiedAppsUtil.stateMachine.examples.abstracted;


import org.gentrifiedApps.gentrifiedAppsUtil.stateMachine.StateMachine;

public class abstractedSM {

    public static StateMachine<StateMachineAbstracted.state> machine() {
        StateMachine.Builder<StateMachineAbstracted.state> builder = new StateMachine.Builder<>();
        return builder
                .state(StateMachineAbstracted.state.StateOne)
                .onEnter(StateMachineAbstracted.state.StateOne, () -> {
                    // do something on enter
                })
                .whileState(StateMachineAbstracted.state.StateOne, () -> {// escapeCondition
                    return true; // return true or false
                }, () -> {
                    // while a condition is false, do something
                })
                .transition(StateMachineAbstracted.state.StateOne, () -> {
                    return true; // return true or false
                }, 0)
                .state(StateMachineAbstracted.state.END)
                .onEnter(StateMachineAbstracted.state.END, () -> {
                    // do something on enter
                })
                .whileState(StateMachineAbstracted.state.END, () -> {
                    return true; // return true or false
                }, () -> {
                    // while a condition is false, do something
                })
                .transition(StateMachineAbstracted.state.END, () -> {
                    return true; // return true or false
                }, 0)
                .stopRunning(StateMachineAbstracted.state.STOP) // stop the state machine
                .build();
    }
}