package org.gentrifiedApps.gentrifiedAppsUtil.stateMachine;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ParallelRunSM<T extends Enum<T>> {
    private List<T> states;
    private Map<T, StateChangeCallback> onEnterCommands;
    private Supplier<Boolean> exitTransition;
    private boolean isStarted = false;
    private boolean isRunning = true;
    private long startTime;

    private AbstractMap.SimpleEntry<Boolean, Integer> timeout;

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isStarted() {
        return isStarted;
    }

    ParallelRunSM(ParallelRunSM.Builder<T> builder) {
        this.states = builder.states;
        this.exitTransition = builder.exitTransition;
        this.onEnterCommands = builder.onEnterCommands;
        this.timeout = builder.timeout;
    }

    public static class Builder<T extends Enum<T>> {
        private List<T> states;
        private Map<T, StateChangeCallback> onEnterCommands;
        private Supplier<Boolean> exitTransition;
        private ParallelRunSM<T> machine;
        private int stopRunningIncluded = 0;
        private AbstractMap.SimpleEntry<Boolean, Integer> timeout;

        public Builder() {
            states = new ArrayList<>();
            onEnterCommands = new HashMap<>();
        }

        public ParallelRunSM.Builder<T> state(T state) {
            if (states.contains(state)) {
                throw new IllegalArgumentException("State already exists");
            }
            states.add(state);
            return this;
        }

        public ParallelRunSM.Builder<T> onEnter(T state, StateChangeCallback command) {
            if (!states.contains(state)) {
                throw new IllegalArgumentException("State does not exist");
            }
            onEnterCommands.put(state, command);
            return this;
        }

        public ParallelRunSM.Builder<T> stopRunning(T state, Supplier<Boolean> exitCommand) {
            this.stopRunningIncluded++;
            if (states.contains(state)) {
                throw new IllegalArgumentException("State already exists");
            }
            states.add(state);
            onEnterCommands.put(state, () -> {
                if (exitCommand.get()) {
                    this.machine.isRunning = false;
                }
            });
            this.exitTransition = exitCommand;
            return this;
        }

        public ParallelRunSM<T> build(Boolean useTimeout, Integer timeout) {
            this.timeout = new AbstractMap.SimpleEntry<>(useTimeout, timeout);

            if (timeout < 0) {
                throw new IllegalArgumentException("Timeout must be a positive integer");
            }

            if (states == null || states.isEmpty()) {
                throw new IllegalArgumentException("States cannot be null or empty");
            }

            if (new HashSet<>(states).size() != states.size()) {
                throw new IllegalArgumentException("States cannot have duplicates");
            }

            if (onEnterCommands.isEmpty()) {
                throw new IllegalArgumentException("States must have corresponding onEnter commands");
            }
            if (onEnterCommands.size() != states.size()) {
                throw new IllegalArgumentException("Not all states have corresponding onEnter commands");
            }

            if (onEnterCommands.get(states.get(0)) == null) {
                throw new IllegalArgumentException("Initial state must have a corresponding onEnter command");
            }
            if (this.stopRunningIncluded != 1) {
                throw new IllegalArgumentException("Not enough or too many stopRunning commands");
            }
            if (this.exitTransition == null) {
                throw new IllegalArgumentException("Exit transition must be set");
            }
            this.machine = new ParallelRunSM<T>(this);
            return this.machine;
        }
    }

    public void start() {
        if (isStarted) {
            throw new IllegalStateException("StateMachine has already been started");
        }
        isStarted = true;
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        if (!isRunning) {
            throw new IllegalStateException("StateMachine is already stopped");
        }
        isRunning = false;
        //delete all actions
        states.clear();
        onEnterCommands.clear();
    }

    public boolean update() {
        if (!states.isEmpty()) {
            // run all states at once
            for (T state : states) {
                StateChangeCallback onEnterAction = onEnterCommands.get(state);
                if (onEnterAction != null) {
                    onEnterAction.onStateChange();
                }
            }
        }
        if (checkExitTransition()) {
            isRunning = false;
        }
        return true;
    }

    public boolean checkExitTransition() {
        boolean exitResult = exitTransition.get();
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Checking exit transition: " + exitResult);
        System.out.println("Elapsed time: " + elapsedTime + "ms");
        final boolean condition = exitResult || (timeout.getKey() && elapsedTime > timeout.getValue());
        if (condition) {
            isRunning = false;
        }
        return condition;
    }
}
