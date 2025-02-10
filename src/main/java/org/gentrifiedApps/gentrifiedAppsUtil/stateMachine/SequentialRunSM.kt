package org.gentrifiedApps.gentrifiedAppsUtil.stateMachine

class SequentialRunSM<T : Enum<T>>(builder: Builder<T>) {
    private var states: List<T> = builder.states
    private var onEnterCommands: Map<T, StateChangeCallback> = builder.onEnterCommands
    private var transitions: Map<T, () -> Boolean> = builder.transitions
    private var transitionDelayTimes: Map<T, Double> = builder.delayTimes
    var currentState: T? = null
    private var stateHistory: MutableList<T> = ArrayList()
    private var isStarted = false
    var isRunning = true
    private var shouldRestart = true
    private var sustainOnEnter: Map<T, StateChangeCallback> = HashMap(builder.onEnterCommands)
    public var sustainStates: List<T> = ArrayList(builder.states)
    private var sustainTransitions: Map<T, () -> Boolean> = HashMap(builder.transitions)

    class Builder<T : Enum<T>> {
        var states: MutableList<T> = ArrayList()
        var onEnterCommands: MutableMap<T, StateChangeCallback> = HashMap()
        var transitions: MutableMap<T, () -> Boolean> = HashMap()
        var delayTimes: MutableMap<T, Double> = HashMap()
        private var machine: SequentialRunSM<T>? = null
        private var stopRunningIncluded = 0

        fun state(state: T): Builder<T> {
            if (states.contains(state)) {
                throw IllegalArgumentException("State already exists")
            }
            states.add(state)
            return this
        }

        fun onEnter(state: T, command: StateChangeCallback): Builder<T> {
            if (!states.contains(state)) {
                throw IllegalArgumentException("State does not exist")
            }
            onEnterCommands[state] = command
            return this
        }

        fun transition(state: T, condition: () -> Boolean, delaySeconds: Double): Builder<T> {
            if (!states.contains(state)) {
                throw IllegalArgumentException("State does not exist")
            }
            if (delaySeconds < 0) {
                throw IllegalArgumentException("Delay cannot be negative")
            }
            transitions[state] = condition
            delayTimes[state] = delaySeconds
            return this
        }

        fun stopRunning(state: T): Builder<T> {
            stopRunningIncluded++
            if (states.contains(state)) {
                throw IllegalArgumentException("State already exists")
            }
            states.add(state)
            if (!states.contains(state)) {
                throw IllegalArgumentException("State does not exist")
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

        fun build(): SequentialRunSM<T> {
            if (states.isEmpty() || transitions.isEmpty()) {
                throw IllegalArgumentException("States and transitions cannot be null or empty")
            }
            if (states.toSet().size != states.size) {
                throw IllegalArgumentException("States cannot have duplicates")
            }
            if (states.size != transitions.size) {
                throw IllegalArgumentException("Mismatched states and transitions")
            }
            if (onEnterCommands[states[0]] == null) {
                throw IllegalArgumentException("Initial state must have a corresponding onEnter command")
            }
            if (stopRunningIncluded != 1) {
                if (stopRunningIncluded == 0) {
                    throw IllegalArgumentException("Missing stopRunning command")
                } else {
                    throw IllegalArgumentException("Too many stopRunning commands")
                }
            }
            machine = SequentialRunSM(this)
            return machine!!
        }
    }

    fun start() {
        if (isStarted) {
            throw IllegalStateException("StateMachine has already been started")
        }
        isStarted = true
        shouldRestart = true
        isRunning = true
        if (states.isNotEmpty()) {
            currentState = states[0]
            onEnterCommands[currentState]?.onStateChange()
        }
    }

    fun stop() {
        if (!isRunning) {
            throw IllegalStateException("StateMachine is already stopped")
        }
        isRunning = false
        states = emptyList()
        onEnterCommands = emptyMap()
        transitions = emptyMap()
    }

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

    fun isValidTransition(fromState: T, toState: T): Boolean {
        if (fromState == toState) {
            println("Cannot transition to itself")
            throw IllegalArgumentException("Cannot transition to itself")
        }
        if (!states.contains(fromState) && !stateHistory.contains(fromState)) {
            println("Cannot transition to itself")
            throw IllegalArgumentException("$fromState does not exist in the state machine")
        }
        if (!states.contains(toState)) {
            println("Cannot transition to itself")
            throw IllegalArgumentException("$toState does not exist in the state machine")
        }
        val transitionCondition = transitions[fromState]
        if (transitionCondition == null) {
            println("Cannot transition to itself")
            throw IllegalStateException("No transition condition exists from state $fromState")
        }
        return transitionCondition()
    }

    fun getStateHistory(): List<T> {
        return stateHistory
    }
}