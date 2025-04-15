package org.gentrifiedApps.gentrifiedAppsUtil.stateMachine;

import org.gentrifiedApps.gentrifiedAppsUtil.classes.except.ExceptionThrower;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * ParallelRunSM is a state machine that runs all states at once. It is used to run multiple states in parallel.
 *
 * @param <T>
 */
public class ParallelRunSM<T extends Enum<T>> {
    private final List<T> states;
    private final Map<T, StateChangeCallback> onEnterCommands;
    private final Supplier<Boolean> exitTransition;
    private final AbstractMap.SimpleEntry<Boolean, Integer> timeout;
    private boolean isStarted = false;
    private boolean isRunning = true;
    private long startTime;

    ParallelRunSM(ParallelRunSM.Builder<T> builder) {
        this.states = builder.states;
        this.exitTransition = builder.exitTransition;
        this.onEnterCommands = builder.onEnterCommands;
        this.timeout = builder.timeout;
    }

    private static void throwException(Exception e) {
        ExceptionThrower.throwException("ParallelRunSM", e);
    }

    /**
     * Returns whether the state machine is running
     *
     * @return boolean if the state machine is running
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Returns whether the state machine has been started
     *
     * @return boolean if the state machine has been started
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * Starts the state machine
     */
    public void start() {
        if (isStarted) {
            throwException(new IllegalStateException("StateMachine has already been started"));
        }
        isStarted = true;
        startTime = System.currentTimeMillis();
    }

    /**
     * Stops the state machine
     */
    public void stop() {
        if (!isRunning) {
            throwException(new IllegalStateException("StateMachine is already stopped"));
        }
        isRunning = false;
        //delete all actions
        states.clear();
        onEnterCommands.clear();
    }

    /**
     * Updates the state machine, should only be done once
     *
     * @return boolean if the state machine has been updated
     */
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

    /**
     * Checks if the exit transition has been met, aka if the state machine should stop running OR if the timeout has been reached
     *
     * @return boolean if the exit transition has been met
     */
    public boolean checkExitTransition() {
        boolean exitResult = exitTransition.get();
        long elapsedTime = System.currentTimeMillis() - startTime;
//        System.out.println("Checking exit transition: " + exitResult);
//        System.out.println("Elapsed time: " + elapsedTime + "ms");
        final boolean condition = exitResult || (timeout.getKey() && elapsedTime > timeout.getValue());
        if (condition) {
            isRunning = false;
        }
        return condition;
    }

    public static class Builder<T extends Enum<T>> {
        private final List<T> states;
        private final Map<T, StateChangeCallback> onEnterCommands;
        private Supplier<Boolean> exitTransition;
        private ParallelRunSM<T> machine;
        private int stopRunningIncluded = 0;
        private AbstractMap.SimpleEntry<Boolean, Integer> timeout;

        /**
         * Constructor for the Builder class
         */
        public Builder() {
            states = new ArrayList<>();
            onEnterCommands = new HashMap<>();
        }

        /**
         * Adds a state to the state machine
         *
         * @param state the state to add
         */
        public ParallelRunSM.Builder<T> state(T state) {
            if (states.contains(state)) {
                throwException(new IllegalArgumentException("State already exists"));
            }
            states.add(state);
            return this;
        }

        /**
         * Adds an onEnter command to a state
         *
         * @param state   the state to add the command to
         * @param command the command to add
         */
        public ParallelRunSM.Builder<T> onEnter(T state, StateChangeCallback command) {
            if (!states.contains(state)) {
                throwException(new IllegalArgumentException("State does not exist"));
            }
            onEnterCommands.put(state, command);
            return this;
        }

        /**
         * Adds a stopRunning command to the state machine
         *
         * @param state       the state to add the command to
         * @param exitCommand the command to add
         */
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

        /**
         * Builds the state machine
         *
         * @param useTimeout whether to use a timeout
         * @param timeout    the timeout to use
         * @return the state machine
         */
        public ParallelRunSM<T> build(Boolean useTimeout, Integer timeout) {
            this.timeout = new AbstractMap.SimpleEntry<>(useTimeout, timeout);

            if (timeout < 0) {
                throwException(new IllegalArgumentException("Timeout must be a positive integer"));
            }

            if (states == null || states.isEmpty()) {
                throwException(new IllegalArgumentException("States cannot be null or empty"));
            }

            if (new HashSet<>(states).size() != states.size()) {
                throwException(new IllegalArgumentException("States cannot have duplicates"));
            }

            if (onEnterCommands.isEmpty()) {
                throwException(new IllegalArgumentException("States must have corresponding onEnter commands"));
            }
            if (onEnterCommands.size() != states.size()) {
                throwException(new IllegalArgumentException("Not all states have corresponding onEnter commands"));
            }

            if (onEnterCommands.get(states.get(0)) == null) {
                throwException(new IllegalArgumentException("Initial state must have a corresponding onEnter command"));
            }
            if (this.stopRunningIncluded != 1) {
                throwException(new IllegalArgumentException("Not enough or too many stopRunning commands"));
            }
            if (this.exitTransition == null) {
                throwException(new IllegalArgumentException("Exit transition must be set"));
            }
            this.machine = new ParallelRunSM<T>(this);
            return this.machine;
        }
    }
}
