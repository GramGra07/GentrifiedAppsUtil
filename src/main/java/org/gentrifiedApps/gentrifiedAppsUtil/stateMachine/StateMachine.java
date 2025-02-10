package org.gentrifiedApps.gentrifiedAppsUtil.stateMachine;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.gentrifiedApps.gentrifiedAppsUtil.stateMachine.StateChangeCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class StateMachine<T extends Enum<T>> {
    private List<T> states;
    private Map<T, StateChangeCallback> onEnterCommands;
    private Map<T, StateChangeCallback> onExitCommands;
    private Map<T, Supplier<Boolean>> transitions;
    private Map<T, StateChangeCallback> whileStateCommands;
    private Map<T, Supplier<Boolean>> whileStateEscapeConditions;
    private Map<T, Double> transitionDelayTimes;
    private T currentState;
    private int currentStateIndex;
    private List<T> stateHistory;
    private boolean isStarted = false;
    private boolean isRunning = true;

    public T getCurrentState() {
        return currentState;
    }

    public boolean mainLoop(LinearOpMode opMode) {
        return opMode.opModeIsActive() && isRunning() && !opMode.isStopRequested();
    }

    public boolean isRunning() {
        return isRunning;
    }

    StateMachine(Builder<T> builder) {
        this.states = builder.states;
        this.onEnterCommands = builder.onEnterCommands;
        this.onExitCommands = builder.onExitCommands;
        this.transitions = builder.transitions;
        this.whileStateCommands = builder.whileStateCommands;
        this.whileStateEscapeConditions = builder.whileStateEscapeConditions;
        this.transitionDelayTimes = builder.delayTimes;
        this.currentState = null;
        this.stateHistory = new ArrayList<>();
    }

    public static class Builder<T extends Enum<T>> {
        private List<T> states;
        private Map<T, StateChangeCallback> onEnterCommands;
        private Map<T, StateChangeCallback> onExitCommands;
        private Map<T, Supplier<Boolean>> transitions;
        private Map<T, StateChangeCallback> whileStateCommands;
        private Map<T, Supplier<Boolean>> whileStateEscapeConditions;
        private Map<T, Double> delayTimes;
        private StateMachine<T> machine;
        private int stopRunningIncluded = 0;

        public Builder() {
            states = new ArrayList<>();
            onEnterCommands = new HashMap<>();
            onExitCommands = new HashMap<>();
            transitions = new HashMap<>();
            whileStateCommands = new HashMap<>();
            whileStateEscapeConditions = new HashMap<>();
            delayTimes = new HashMap<>();
        }

        public Builder<T> state(T state) {
            if (states.contains(state)) {
                throw new IllegalArgumentException("State already exists");
            }
            states.add(state);
            return this;
        }

        public Builder<T> whileState(T state, Supplier<Boolean> escapeCondition, StateChangeCallback command) {
            whileStateCommands.put(state, command); // Store the command
            whileStateEscapeConditions.put(state, escapeCondition); // Store the escape condition
            return this;
        }

        public Builder<T> onEnter(T state, StateChangeCallback command) {
            if (!states.contains(state)) {
                throw new IllegalArgumentException("State does not exist");
            }
            onEnterCommands.put(state, command);
            return this;
        }

        public Builder<T> onExit(T state, StateChangeCallback command) {
            if (!states.contains(state)) {
                throw new IllegalArgumentException("State does not exist");
            }
            onExitCommands.put(state, command);
            return this;
        }

        public Builder<T> transition(T state, Supplier<Boolean> condition, double delaySeconds) {
            if (!states.contains(state)) {
                throw new IllegalArgumentException("State does not exist");
            }
            if (delaySeconds < 0) {
                throw new IllegalArgumentException("Delay cannot be negative");
            }
            transitions.put(state, condition);
            delayTimes.put(state, delaySeconds);
            return this;
        }

        public Builder<T> stopRunning(T state) {
            this.stopRunningIncluded++;
            if (states.contains(state)) {
                throw new IllegalArgumentException("State already exists");
            }
            states.add(state);
            if (!states.contains(state)) {
                throw new IllegalArgumentException("State does not exist");
            }
            onEnterCommands.put(state, () -> {
                this.machine.isRunning = false;
            });
            transitions.put(state, () -> {
                this.machine.isRunning = false;
                return true;
            });
            return this;
        }

        public StateMachine<T> build() {
            if (states == null || states.isEmpty() || transitions == null || transitions.isEmpty()) {
                throw new IllegalArgumentException("States and transitions cannot be null or empty");
            }

            if (new HashSet<>(states).size() != states.size()) {
                throw new IllegalArgumentException("States cannot have duplicates");
            }

            if (states.size() != transitions.size()) {
                throw new IllegalArgumentException("Mismatched states and transitions");
            }

            if (onEnterCommands.get(states.get(0)) == null) {
                throw new IllegalArgumentException("Initial state must have a corresponding onEnter command");
            }
            if (this.stopRunningIncluded != 1) {
                if (this.stopRunningIncluded == 0) {
                    throw new IllegalArgumentException("Missing stopRunning command");
                } else {
                    throw new IllegalArgumentException("Too many stopRunning commands");
                }
            }
            this.machine = new StateMachine<>(this);
            return this.machine;
        }
    }

    public void start() {
        if (isStarted) {
            throw new IllegalStateException("StateMachine has already been started");
        }
        isStarted = true;
        if (!states.isEmpty()) {
            currentState = states.get(0);
            StateChangeCallback onEnterAction = onEnterCommands.get(currentState);
            if (onEnterAction != null) {
                onEnterAction.onStateChange();
            }
        }
    }

    public void stop() {
        isRunning = false;
        stateHistory.clear();
        currentState = null;
        isStarted = false;
        currentActionType = TYPES.IDLE;
        currentStateIndex = 9999999;
    }

    private double startTime = 0;

    enum TYPES {
        IDLE,
        ON_ENTER,
        WHILE_STATE,
        ON_EXIT,
        TRANSITION,
        STOP,
    }

    private TYPES lastActionType;
    private TYPES currentActionType = TYPES.IDLE;
    public boolean update() {
        if (currentActionType == TYPES.IDLE && isStarted) {
            currentActionType = TYPES.ON_ENTER;
        }
        if (currentState == null || !isRunning) {
            return false;
        }

        // Get the actions and conditions for the current state
        StateChangeCallback onEnterAction = onEnterCommands.get(currentState);
        StateChangeCallback whileStateAction = whileStateCommands.get(currentState);
        Supplier<Boolean> escapeCondition = whileStateEscapeConditions.get(currentState);
        StateChangeCallback onExitAction = onExitCommands.get(currentState);
        Supplier<Boolean> transitionCondition = transitions.get(currentState);
        Double delayTime = transitionDelayTimes.getOrDefault(currentState, 0.0);

        // Handle STOP type
        if (currentActionType == TYPES.STOP) {
            stop();
            return false;
        }

        // Execute onEnter if transitioning into the state
        if (currentActionType == TYPES.ON_ENTER) {
            if (onEnterAction != null) {
                onEnterAction.onStateChange();
            }
            currentActionType = (whileStateAction != null) ? TYPES.WHILE_STATE : TYPES.TRANSITION;
            return true;
        }

        // Execute whileState if defined
        if (currentActionType == TYPES.WHILE_STATE) {
            if (whileStateAction != null) {
                whileStateAction.onStateChange();
            }
            if (escapeCondition != null && escapeCondition.get()) {
                currentActionType = TYPES.ON_EXIT;
            } else {
                return true;
            }
        }

        // Execute onExit if transitioning out of the state
        if (currentActionType == TYPES.ON_EXIT) {
            if (onExitAction != null) {
                onExitAction.onStateChange();
            }
            currentActionType = TYPES.TRANSITION;
            return true;
        }

        // Handle transition logic
        if (currentActionType == TYPES.TRANSITION) {
            if (transitionCondition != null && transitionCondition.get()) {
                int nextIndex = currentStateIndex + 1;
                isValidTransition(currentState, states.get(nextIndex));
                if (nextIndex < states.size()) {
                    // Handle transition delay
                    if (delayTime > 0) {
                        double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;
                        if (startTime == 0 || elapsedTime < delayTime) {
                            if (startTime == 0) {
                                startTime = System.currentTimeMillis();
                            }
                            return true; // Wait until delay passes
                        }
                    }
                    startTime = 0; // Reset delay timer

                    // Transition to the next state
                    stateHistory.add(currentState);
                    currentState = states.get(nextIndex);
                    currentStateIndex = nextIndex;
                    currentActionType = TYPES.ON_ENTER;
                    return true;
                } else {
                    currentActionType = TYPES.STOP; // Transition to STOP
                    return false;
                }
            }
        }

        return false; // No actions performed
    }



    public boolean isValidTransition(T fromState, T toState) {
        if (fromState == toState) {
            throw new IllegalArgumentException("Cannot transition to itself");
        }
        if (!states.contains(fromState) && !stateHistory.contains(fromState)) {
            throw new IllegalArgumentException(fromState + " does not exist in the state machine");
        }
        if (!states.contains(toState)) {
            throw new IllegalArgumentException(toState + " does not exist in the state machine");
        }
        Supplier<Boolean> transitionCondition = transitions.get(fromState);
        if (transitionCondition == null) {
            throw new IllegalStateException("No transition condition exists from state " + fromState);
        }
        return transitionCondition.get();
    }

    public List<T> getStateHistory() {
        return stateHistory;
    }
}