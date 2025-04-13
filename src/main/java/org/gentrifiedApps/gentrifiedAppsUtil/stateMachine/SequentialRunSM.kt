package org.gentrifiedApps.gentrifiedAppsUtil.stateMachine

import org.gentrifiedApps.gentrifiedAppsUtil.classes.except.ExceptionThrower

/**
 * A class to represent a sequential state machine
 * @param T The enum class for the states
 * @param builder The builder for the state machine
 * @see Builder
 */
class SequentialRunSM<T : Enum<T>>(builder: Builder<T>) {
    private var states: List<T> = builder.states
    private var onEnterCommands: Map<T, StateChangeCallback> = builder.onEnterCommands
    private var transitions: Map<T, () -> Boolean> = builder.transitions
    private var transitionDelayTimes: Map<T, Double> = builder.delayTimes
    internal var currentState: T? = null
    private var stateHistory: MutableList<T> = ArrayList()
    internal var isStarted = false
    internal var isRunning = true
    private var shouldRestart = true
    private var sustainOnEnter: Map<T, StateChangeCallback> = HashMap(builder.onEnterCommands)
    internal var sustainStates: List<T> = ArrayList(builder.states)
    private var sustainTransitions: Map<T, () -> Boolean> = HashMap(builder.transitions)

    class Builder<T : Enum<T>> {
        internal var states: MutableList<T> = ArrayList()
        internal var onEnterCommands: MutableMap<T, StateChangeCallback> = HashMap()
        internal var transitions: MutableMap<T, () -> Boolean> = HashMap()
        internal var delayTimes: MutableMap<T, Double> = HashMap()
        private var machine: SequentialRunSM<T>? = null
        private var stopRunningIncluded = 0

        /**
         * Adds a state to the state machine
         * @param state The state to add
         */
        fun state(state: T): Builder<T> {
            if (states.contains(state)) {
                throwException(IllegalArgumentException("State already exists"))
            }
            states.add(state)
            return this
        }

        /**
         * Adds an onEnter command to a state
         * @param state The state to add the command to
         * @param command The command to run
         */
        fun onEnter(state: T, command: StateChangeCallback): Builder<T> {
            if (!states.contains(state)) {
                throwException(IllegalArgumentException("State does not exist"))
            }
            onEnterCommands[state] = command
            return this
        }

        /**
         * Adds a transition to a state
         * @param state The state to add the transition to
         * @param condition The condition to transition
         */
        fun transition(state: T, condition: () -> Boolean, delaySeconds: Double): Builder<T> {
            if (!states.contains(state)) {
                throwException(IllegalArgumentException("State does not exist"))
            }
            if (delaySeconds < 0) {
                throwException(IllegalArgumentException("Delay cannot be negative"))
            }
            transitions[state] = condition
            delayTimes[state] = delaySeconds
            return this
        }

        /**
         * Adds a stopRunning command to a state
         * @param state The state to add the command to
         */
        fun stopRunning(state: T): Builder<T> {
            stopRunningIncluded++
            if (states.contains(state)) {
                throwException(IllegalArgumentException("State already exists"))
            }
            states.add(state)
            if (!states.contains(state)) {
                throwException(IllegalArgumentException("State does not exist"))
            }
            onEnterCommands[state] = StateChangeCallback {
                machine!!.isRunning = false
            }
            transitions[state] = {
                machine!!.isRunning = false
                true
            }
            return this
        }

        /**
         * Builds the state machine
         * @return The state machine
         */
        fun build(): SequentialRunSM<T> {
            if (states.isEmpty() || transitions.isEmpty()) {
                throwException(IllegalArgumentException("States and transitions cannot be null or empty"))
            }
            if (states.toSet().size != states.size) {
                throwException(IllegalArgumentException("States cannot have duplicates"))
            }
            if (states.size != transitions.size) {
                throwException(IllegalArgumentException("Mismatched states and transitions"))
            }
            if (onEnterCommands[states[0]] == null) {
                throwException(IllegalArgumentException("Initial state must have a corresponding onEnter command"))
            }
            if (stopRunningIncluded != 1) {
                if (stopRunningIncluded == 0) {
                    throwException(IllegalArgumentException("Missing stopRunning command"))
                } else {
                    throwException(IllegalArgumentException("Too many stopRunning commands"))
                }
            }
            machine = SequentialRunSM(this)
            return machine!!
        }

        private fun throwException(e: Exception) {
            ExceptionThrower.throwException("SequentialRunSM", e)
        }
    }

    private fun throwException(e: Exception) {
        ExceptionThrower.throwException("SequentialRunSM", e)
    }

    /**
     * Starts the state machine
     */
    fun start() {
        if (isStarted) {
            throwException(IllegalStateException("StateMachine has already been started"))
        }
        isStarted = true
        shouldRestart = true
        isRunning = true
        if (states.isNotEmpty()) {
            currentState = states[0]
            onEnterCommands[currentState]?.onStateChange()
        }
    }

    /**
     * Stops the state machine
     */
    fun stop() {
        if (!isRunning) {
            throwException(IllegalStateException("StateMachine is already stopped"))
        }
        isRunning = false
        states = emptyList()
        onEnterCommands = emptyMap()
        transitions = emptyMap()
    }

    /**
     * Restarts the state machine
     */
    fun restartAtBeginning() {
        if (shouldRestart) {
            stateHistory.clear()
            isRunning = false
            isStarted = false
            states = sustainStates
            transitions = sustainTransitions
            onEnterCommands = sustainOnEnter
            currentStateIndex = 0
            shouldRestart = false
        }
    }

    private var currentActionType = StateMachine.TYPES.IDLE
    private var currentStateIndex = 0
    private var startTime = 0.0

    /**
     * Updates the state machine
     * @return Whether the state machine has updated successfully
     */
    fun update(): Boolean {
        if (currentActionType == StateMachine.TYPES.IDLE && isStarted) {
            currentActionType = StateMachine.TYPES.ON_ENTER
        }
        if (currentState == null || !isRunning) {
            return false
        }

        val onEnterAction = onEnterCommands[currentState]

        val transitionCondition = transitions[currentState]
        val delayTime = transitionDelayTimes.getOrDefault(currentState, 0.0)

        if (currentActionType == StateMachine.TYPES.ON_ENTER) {
            onEnterAction?.onStateChange()
            currentActionType = StateMachine.TYPES.TRANSITION
            return true
        }
        if (currentActionType == StateMachine.TYPES.STOP) {
            stop()
            return false
        }
        if (currentActionType == StateMachine.TYPES.TRANSITION) {
            if (transitionCondition != null && transitionCondition()) {
                val nextIndex = currentStateIndex + 1
                isValidTransition(currentState!!, states[nextIndex])
                if (nextIndex < states.size) {
                    if (delayTime > 0) {
                        val elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0
                        if (startTime == 0.0 || elapsedTime < delayTime) {
                            if (startTime == 0.0) {
                                startTime = System.currentTimeMillis().toDouble()
                            }
                            return true
                        }
                    }
                    startTime = 0.0
                    stateHistory.add(currentState!!)
                    currentState = states[nextIndex]
                    currentStateIndex = nextIndex
                    currentActionType = StateMachine.TYPES.ON_ENTER
                    return true
                } else {
                    currentActionType = StateMachine.TYPES.STOP
                    return false
                }
            }
        }
        return false
    }

    private fun isValidTransition(fromState: T, toState: T): Boolean {
        if (fromState == toState) {
            println("Cannot transition to itself")
            throwException(IllegalArgumentException("Cannot transition to itself"))
        }
        if (!states.contains(fromState) && !stateHistory.contains(fromState)) {
            println("Cannot transition to itself")
            throwException(IllegalArgumentException("$fromState does not exist in the state machine"))
        }
        if (!states.contains(toState)) {
            println("Cannot transition to itself")
            throwException(IllegalArgumentException("$toState does not exist in the state machine"))
        }
        val transitionCondition = transitions[fromState]
        if (transitionCondition == null) {
            println("Cannot transition to itself")
            throwException(IllegalStateException("No transition condition exists from state $fromState"))
        }
        return transitionCondition?.invoke() == true
    }
}