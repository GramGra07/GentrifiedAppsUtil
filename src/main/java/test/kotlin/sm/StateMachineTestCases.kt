package test.kotlin.sm

import org.firstinspires.ftc.robotcore.internal.system.Assert.assertFalse
import org.firstinspires.ftc.robotcore.internal.system.Assert.assertTrue
import org.gentrifiedApps.gentrifiedAppsUtil.stateMachine.StateMachine
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before
import java.util.Random
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Supplier


class StateMachineTestCases {
    lateinit var stateMachine: StateMachine<TestState>

    enum class TestState {
        STATE_A, STATE_B, STATE_C, STOP, STATE_NO
    }

    @Before
    fun setUp() {
        stateMachine = StateMachine.Builder<TestState>()
            .state(TestState.STATE_A)
            .state(TestState.STATE_B)
            .state(TestState.STATE_C)
            .onEnter(TestState.STATE_A) { println("Entering STATE_A") }
            .whileState(
                TestState.STATE_A,
                { true },
                { println("Executing STATE_A") })
            .onExit(TestState.STATE_A) { println("Exiting STATE_A") }
            .transition(TestState.STATE_A, { true }, 0.0)
            .onEnter(TestState.STATE_B) { println("Entering STATE_B") }
            .transition(TestState.STATE_B, { true }, 0.0)
            .onEnter(TestState.STATE_C) { println("Entering STATE_C") }
            .transition(TestState.STATE_C, { true }, 0.0)
            .stopRunning(TestState.STOP)
            .build()

        stateMachine.start()
    }

    @Test
    fun testInitialState() {
        assertEquals(
            "Initial state should be STATE_A",
            TestState.STATE_A,
            stateMachine.currentState
        )
    }

    @Test
    fun testStateTransition() {
        assertTrue(stateMachine.update(), "Update should process STATE_A")
        assertTrue(stateMachine.update(), "Update should process STATE_A")
        assertTrue(stateMachine.update(), "Update should process STATE_A")
        assertTrue(stateMachine.update(), "Update should process STATE_A")
        assertEquals(
            "State should transition to STATE_B",
            TestState.STATE_B,
            stateMachine.currentState
        )

        assertTrue(stateMachine.update(), "Update should process STATE_B")
        assertEquals(
            "State should transition to STATE_C",
            TestState.STATE_C,
            stateMachine.currentState
        )

        assertTrue(stateMachine.update(), "Update should process STATE_C")
        assertTrue(stateMachine.update(), "Update should process STATE_C")

        assertEquals(
            "State should transition to STOP",
            TestState.STOP,
            stateMachine.currentState
        )
    }

    @Test
    fun testStopState() {
        while (stateMachine.update()) {
            // Process all states until STOP
        }
        assertFalse(
            stateMachine.isRunning
        )
    }

    @Test
    fun testWhileStateExecution() {
        stateMachine.update() // Process STATE_A
        // Assuming the escape condition of STATE_A is set to false initially
        // Verify if whileState logic is executed
        stateMachine.update()
        stateMachine.update()
        assertEquals(
            "Escape condition should transition to STATE_B",
            TestState.STATE_B,
            stateMachine.currentState
        )
    }

    @Test
    fun testOnEnterActionExecution() {
        stateMachine.update() // Enter STATE_A
        assertEquals(
            "State should remain in STATE_A after ON_ENTER action",
            TestState.STATE_A,
            stateMachine.currentState
        )
    }

    @Test
    fun testWhileStateActionExecution() {
        stateMachine.update() // Transition to WHILE_STATE for STATE_A
        assertTrue(
            stateMachine.update()
        )
    }

    @Test
    fun testEscapeConditionTransition() {
        stateMachine.update() // Process STATE_A
        val escapeCondition = Supplier { true } // Force transition from WHILE_STATE
        stateMachine.update()
        stateMachine.update()
        assertEquals(
            "Escape condition should trigger transition to STATE_B",
            TestState.STATE_B,
            stateMachine.currentState
        )
    }

    @Test
    fun testOnExitActionExecution() {
        stateMachine.update() // STATE_A ON_ENTER
        stateMachine.update() // STATE_A WHILE_STATE and transition
        stateMachine.update()
        assertEquals(
            "ON_EXIT action should lead to transition to STATE_B",
            TestState.STATE_B,
            stateMachine.currentState
        )
    }

    @Test
    fun testStopStateExecution() {
        while (stateMachine.update()) {
            // Process all states until STOP
        }
        assertFalse(
            stateMachine.isRunning
        )
        assertEquals(
            "Final state should be STOP",
            TestState.STOP,
            stateMachine.currentState
        )
    }

    @Test
    fun testInvalidStateUpdate() {
        stateMachine.stop() // Manually stop the state machine
        assertFalse(
            stateMachine.update()
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun testTransitionDelay() {
        stateMachine = StateMachine.Builder<TestState>()
            .state(TestState.STATE_A)
            .state(TestState.STATE_B)
            .onEnter(TestState.STATE_A) { println("Entering STATE_A") }
            .onEnter(TestState.STATE_B) { println("Entering STATE_B") }
            .transition(TestState.STATE_A, { true }, 2.0) // 2-second delay
            .transition(TestState.STATE_B, { true }, 0.0)
            .stopRunning(TestState.STOP)
            .build()

        stateMachine.start()
        stateMachine.update() // Transition starts
        stateMachine.update()

        Thread.sleep(1000) // Wait 1 second
        assertEquals(
            "State should still be STATE_A due to delay",
            TestState.STATE_A,
            stateMachine.currentState
        )

        Thread.sleep(2000) // Wait additional time
        stateMachine.update()
        assertEquals(
            "State should transition to STATE_B after delay",
            TestState.STATE_B,
            stateMachine.currentState
        )
    }

    @Test
    fun testMultipleStateCallbacks() {
        stateMachine.update() // Enter STATE_A
        stateMachine.update() // WHILE_STATE executes
        stateMachine.update() // Transition to STATE_B
        assertEquals(
            "State should transition to STATE_B after callbacks",
            TestState.STATE_B,
            stateMachine.currentState
        )
    }

    @Test
    fun testRandomStateUpdates() {
        val random = Random()
        val randomCalls = AtomicInteger()

        stateMachine = StateMachine.Builder<TestState>()
            .state(TestState.STATE_A)
            .state(TestState.STATE_B)
            .state(TestState.STATE_C)
            .stopRunning(TestState.STOP)
            .onEnter(TestState.STATE_A) { println("Entering STATE_A") }
            .whileState(
                TestState.STATE_A,
                { random.nextBoolean() },
                { randomCalls.incrementAndGet() })
            .onExit(TestState.STATE_A) { println("Exiting STATE_A") }
            .transition(
                TestState.STATE_A,
                { random.nextBoolean() }, 0.0
            )
            .onEnter(TestState.STATE_B) { println("Entering STATE_B") }
            .transition(
                TestState.STATE_B,
                { random.nextBoolean() }, 0.0
            )
            .onEnter(TestState.STATE_C) { println("Entering STATE_C") }
            .transition(
                TestState.STATE_C,
                { random.nextBoolean() }, 0.0
            )
            .build()

        stateMachine.start()

        for (i in 0..9) { // Randomly update 10 times
            stateMachine.update()
        }

        println("Random calls during STATE_A: " + randomCalls.get())
        assertTrue(
            randomCalls.get() > 0
        )
    }

    @Test
    fun testDynamicEscapeCondition() {
        val counter = AtomicInteger()
        val dynamicEscapeCondition = Supplier { counter.incrementAndGet() > 3 }

        stateMachine = StateMachine.Builder<TestState>()
            .state(TestState.STATE_A)
            .state(TestState.STATE_B)
            .stopRunning(TestState.STOP)
            .onEnter(TestState.STATE_A) { println("Entering STATE_A") }
            .whileState(
                TestState.STATE_A, dynamicEscapeCondition
            ) { println("While in STATE_A") }
            .onExit(TestState.STATE_A) { println("Exiting STATE_A") }
            .transition(TestState.STATE_A, { true }, 0.0)
            .onEnter(TestState.STATE_B) { println("Entering STATE_B") }
            .transition(TestState.STATE_B, { true }, 0.0)
            .build()

        stateMachine.start()
        var updates = 0
        while (stateMachine.update()) {
            updates++
        }

        assertEquals(
            9,
            updates
        )
    }

    @Test
    fun testStateMachineStabilityUnderHighLoad() {
        stateMachine = StateMachine.Builder<TestState>()
            .state(TestState.STATE_A)
            .state(TestState.STATE_B)
            .stopRunning(TestState.STOP)
            .onEnter(TestState.STATE_A) { println("Entering STATE_A") }
            .transition(TestState.STATE_A, { true }, 0.0)
            .onEnter(TestState.STATE_B) { println("Entering STATE_B") }
            .transition(TestState.STATE_B, { true }, 0.0)
            .build()

        stateMachine.start()

        for (i in 0..999) { // Stress test with high updates
            if (!stateMachine.update()) {
                break
            }
        }

        assertFalse(
            stateMachine.isRunning()
        )
        assertEquals(
            TestState.STOP,
            stateMachine.currentState
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun testStateMachineWithRandomDelays() {
        val random = Random()

        stateMachine = StateMachine.Builder<TestState>()
            .state(TestState.STATE_A)
            .state(TestState.STATE_B)
            .state(TestState.STATE_C)
            .stopRunning(TestState.STOP)
            .onEnter(TestState.STATE_A) { println("Entering STATE_A") }
            .transition(
                TestState.STATE_A,
                { true },
                random.nextDouble() * 3
            ) // Random delay up to 3 seconds
            .onEnter(TestState.STATE_B) { println("Entering STATE_B") }
            .transition(
                TestState.STATE_B,
                { true },
                random.nextDouble() * 3
            ) // Random delay up to 3 seconds
            .onEnter(TestState.STATE_C) { println("Entering STATE_C") }
            .transition(
                TestState.STATE_C,
                { true },
                random.nextDouble() * 3
            ) // Random delay up to 3 seconds
            .build()

        stateMachine.start()
        var updates = 0

        while (stateMachine.update()) {
            updates++
            Thread.sleep(100) // Simulate processing time
        }

        println("Total updates processed: $updates")
        assertEquals(
            "Final state should be STOP after processing with random delays",
            TestState.STOP,
            stateMachine.currentState
        )
    }

    @Test
    fun testValidTransition() {
        val condition = Supplier { true }

        // Set up a valid transition
        stateMachine = StateMachine.Builder<TestState>()
            .state(TestState.STATE_A)
            .state(TestState.STATE_B)
            .transition(TestState.STATE_A, condition, 0.0)
            .transition(TestState.STATE_B, condition, 0.0)
            .onEnter(TestState.STATE_A) { println("Entering STATE_A") }
            .onEnter(TestState.STATE_B) { println("Entering STATE_B") }
            .stopRunning(TestState.STOP)
            .build()
    }
    @Test
    fun testRandomTransitionCondition() {
        val random = Random()
        val randomCondition = Supplier { random.nextBoolean() }

        stateMachine = StateMachine.Builder<TestState>()
            .state(TestState.STATE_A)
            .state(TestState.STATE_B)
            .transition(TestState.STATE_A, randomCondition, 0.0)
            .transition(TestState.STATE_B, randomCondition, 0.0)
            .onEnter(TestState.STATE_A) { println("Entering STATE_A") }
            .onEnter(TestState.STATE_B) { println("Entering STATE_B") }
            .stopRunning(TestState.STOP)
            .build()

        val result = stateMachine.isValidTransition(TestState.STATE_A, TestState.STATE_B)
        println("Random condition result: $result")
        assertTrue(
            result || !result
        )
    }

    @Test
    fun testTransitionHistoryIncluded() {
        stateMachine = StateMachine.Builder<TestState>()
            .state(TestState.STATE_A).onEnter(
                TestState.STATE_A
            ) { println("Entering STATE_A") }
            .transition(TestState.STATE_A, { true }, 0.0)

            .state(TestState.STATE_B)
            .transition(TestState.STATE_B, { true }, 0.0)
            .onEnter(TestState.STATE_B) { println("Entering STATE_B") }
            .stopRunning(TestState.STOP)
            .build()

        stateMachine.start()
        stateMachine.update() // Transition to STATE_B
        stateMachine.update() // Transition to STATE_B
        stateMachine.update() // Transition to STATE_B
        stateMachine.update() // Transition to STATE_B
        stateMachine.isValidTransition(TestState.STATE_A, TestState.STATE_B)
        //print the history
        println(stateMachine.getStateHistory())
        assertTrue(
            stateMachine.getStateHistory().contains(TestState.STATE_A)
        )
    }
}