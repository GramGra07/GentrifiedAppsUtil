package test.kotlin.sm

import org.firstinspires.ftc.robotcore.internal.system.Assert.assertFalse
import org.firstinspires.ftc.robotcore.internal.system.Assert.assertTrue
import org.gentrifiedApps.gentrifiedAppsUtil.stateMachine.SequentialRunSM
import org.junit.Assert.assertEquals
import org.junit.Test
import org.testng.Assert.assertThrows
import java.util.concurrent.atomic.AtomicInteger

internal class SequentialRunSMTestCases {
    private val sqsm: SequentialRunSM<States>? = null

    internal enum class States {
        STATE1,
        STATE2,
        STATE3,
        STOP,
    }

    var booleans: List<Boolean> =
        mutableListOf(false, false, true, false, true, false, true, false, true, false)

    @Test
    fun testBasic() {
        println("Testing basic state machine")
        val builder = SequentialRunSM.Builder<States>()
        val count = AtomicInteger()
        builder.state(States.STATE1).onEnter(
            States.STATE1
        ) {
            println("Entering STATE1")
        }.transition(States.STATE1, { true }, 0.0).state(States.STATE2).onEnter(
            States.STATE2
        ) {
            println("Entering STATE2")
        }.transition(States.STATE2, {
            println("Transitioning from STATE2 to STATE3")
            true
        }, 0.0).state(States.STATE3).onEnter(
            States.STATE3
        ) {
            println("Entering STATE3")
        }.transition(States.STATE3, {
            println("Transitioning from STATE3 to STOP")
            true
        }, 0.0).stopRunning(States.STOP)

        val srsmb = builder.build()
        srsmb.start()
        assertTrue(srsmb.isRunning)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STATE2, srsmb.currentState)
        assertTrue(srsmb.update())
        assertEquals(States.STATE3, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertFalse(srsmb.update())
        assertEquals(States.STOP, srsmb.currentState)
        assertFalse(srsmb.isRunning)

        println("States updated successfully")
    }

    internal enum class AutoLift {
        extend_pivot,
        hook1st,
        lift1st,
        pivotBack,
        sit1st,
        extend2nd,
        hook2nd,
        lift2nd,
        pivot2nd,
        sit2nd,
        collapse,
        stop
    }

    @Test
    fun testLiftSequence() {
        val builder = SequentialRunSM.Builder<AutoLift>()
        builder.state(AutoLift.extend_pivot)
            .onEnter(AutoLift.extend_pivot) {}
            .transition(AutoLift.extend_pivot, { true }, 0.0)
            .state(AutoLift.hook1st)
            .onEnter(AutoLift.hook1st) {}
            .transition(AutoLift.hook1st, { true }, 0.0)
            .state(AutoLift.lift1st)
            .onEnter(AutoLift.lift1st) {}
            .transition(AutoLift.lift1st, { true }, 0.0)
            .state(AutoLift.pivotBack)
            .onEnter(AutoLift.pivotBack) {}
            .transition(AutoLift.pivotBack, { true }, 0.0)
            .state(AutoLift.sit1st)
            .onEnter(AutoLift.sit1st) {}
            .transition(AutoLift.sit1st, { true }, 0.0)
            .state(AutoLift.extend2nd)
            .onEnter(AutoLift.extend2nd) {}
            .transition(AutoLift.extend2nd, { true }, 0.0)
            .state(AutoLift.hook2nd)
            .onEnter(AutoLift.hook2nd) {}
            .transition(AutoLift.hook2nd, { true }, 0.0)
            .state(AutoLift.lift2nd)
            .onEnter(AutoLift.lift2nd) {}
            .transition(AutoLift.lift2nd, { true }, 0.0)
            .state(AutoLift.pivot2nd)
            .onEnter(AutoLift.pivot2nd) {}
            .transition(AutoLift.pivot2nd, { true }, 0.0)
            .state(AutoLift.sit2nd)
            .onEnter(AutoLift.sit2nd) {}
            .transition(AutoLift.sit2nd, { true }, 0.0)
            .state(AutoLift.collapse)
            .onEnter(AutoLift.collapse) {}
            .transition(AutoLift.collapse, { true }, 0.0)
            .stopRunning(AutoLift.stop)

        val liftSequence = builder.build()
        liftSequence.start()

        // Add assertions to verify the state transitions and final state
        assertTrue(liftSequence.isRunning)
        while (liftSequence.isRunning) {
            liftSequence.update()
        }
        assertEquals(AutoLift.stop, liftSequence.currentState)
    }

    @Test
    fun testStopRunning() {
        println("Testing stopRunning")
        val builder = SequentialRunSM.Builder<States>()
        builder.state(States.STATE1).onEnter(
            States.STATE1
        ) {
            println("Entering STATE1")
        }.transition(States.STATE1, { true }, 0.0).state(States.STATE2).onEnter(
            States.STATE2
        ) {
            println("Entering STATE2")
        }.transition(States.STATE2, {
            println("Transitioning from STATE2 to STATE3")
            true
        }, 0.0).state(States.STATE3).onEnter(
            States.STATE3
        ) {
            println("Entering STATE3")
        }.transition(States.STATE3, {
            println("Transitioning from STATE3 to STOP")
            true
        }, 0.0).stopRunning(States.STOP)

        val srsmb = builder.build()
        srsmb.start()
        assertTrue(srsmb.isRunning)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STATE2, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STATE3, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertFalse(srsmb.update())
        assertEquals(States.STOP, srsmb.currentState)
        assertFalse(srsmb.isRunning)
    }

    @Test
    fun testDuplicateState() {
        println("Testing duplicate state")
        val builder = SequentialRunSM.Builder<States>()
        assertThrows(
            IllegalArgumentException::class.java
        ) {
            builder.state(States.STATE1).onEnter(
                States.STATE1
            ) {
                println("Entering STATE1")
            }.transition(States.STATE1, { true }, 0.0)
                .state(States.STATE2).onEnter(
                    States.STATE2
                ) {
                    println("Entering STATE2")
                }.transition(States.STATE2, {
                    println("Transitioning from STATE2 to STATE3")
                    true
                }, 0.0).state(States.STATE3).onEnter(
                    States.STATE3
                ) {
                    println("Entering STATE3")
                }.transition(States.STATE3, {
                    println("Transitioning from STATE3 to STOP")
                    true
                }, 0.0).state(States.STATE3).onEnter(
                    States.STATE3
                ) {
                    println("Entering STATE3")
                }.transition(States.STATE3, {
                    println("Transitioning from STATE3 to STOP")
                    true
                }, 0.0).stopRunning(States.STOP)
        }
    }

    // test that they are done in sequence and not in parallel
    @Test
    fun testSequence() {
        println("Testing sequence")
        val builder = SequentialRunSM.Builder<States>()
        val commandExecuted = booleanArrayOf(false, false)
        builder.state(States.STATE1).onEnter(
            States.STATE1
        ) {
            println("Entering STATE1")
            commandExecuted[0] = true
        }.transition(States.STATE1, { true }, 0.0).state(States.STATE2).onEnter(
            States.STATE2
        ) {
            println("Entering STATE2")
            commandExecuted[1] = true
        }.transition(States.STATE2, {
            println("Transitioning from STATE2 to STATE3")
            true
        }, 0.0).state(States.STATE3).onEnter(
            States.STATE3
        ) {
            println("Entering STATE3")
            commandExecuted[0] = false
        }.transition(States.STATE3, {
            println("Transitioning from STATE3 to STOP")
            commandExecuted[1] = false
            true
        }, 0.0).stopRunning(States.STOP)

        val srsmb = builder.build()
        srsmb.start()
        assertTrue(srsmb.isRunning)
        assertTrue(commandExecuted[0])
        assertFalse(commandExecuted[1])
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertTrue(commandExecuted[0])
        assertTrue(commandExecuted[1])
        assertEquals(States.STATE2, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertFalse(commandExecuted[0])
        assertTrue(commandExecuted[1])
        assertEquals(States.STATE3, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertFalse(commandExecuted[0])
        assertFalse(commandExecuted[1])
        assertEquals(States.STOP, srsmb.currentState)
        assertFalse(srsmb.isRunning)
    }

    @Test
    fun testEmptyStates() {
        println("Testing empty states")
        val builder = SequentialRunSM.Builder<States>()
        assertThrows(
            IllegalArgumentException::class.java
        ) { builder.build() }
    }

    // test stop function
    @Test
    fun testStop() {
        println("Testing stop")
        val builder = SequentialRunSM.Builder<States>()
        val count = AtomicInteger()
        builder.state(States.STATE1).onEnter(
            States.STATE1
        ) {
            println("Entering STATE1")
        }.transition(States.STATE1, { true }, 0.0).state(States.STATE2).onEnter(
            States.STATE2
        ) {
            println("Entering STATE2")
        }.transition(States.STATE2, {
            println("Transitioning from STATE2 to STATE3")
            true
        }, 0.0).state(States.STATE3).onEnter(
            States.STATE3
        ) {
            println("Entering STATE3")
        }.transition(States.STATE3, {
            println("Transitioning from STATE3 to STOP")
            true
        }, 0.0).stopRunning(States.STOP)

        val srsmb = builder.build()
        srsmb.start()
        assertTrue(srsmb.isRunning)
        srsmb.stop()
        assertFalse(srsmb.isRunning)
    }

    // comprehensively test all the functionality
    @Test
    fun testComprehensive() {
        println("Testing comprehensive")
        val builder = SequentialRunSM.Builder<States>()
        val count = AtomicInteger()
        builder.state(States.STATE1).onEnter(
            States.STATE1
        ) {
            println("Entering STATE1")
        }.transition(States.STATE1, { true }, 0.0).state(States.STATE2).onEnter(
            States.STATE2
        ) {
            println("Entering STATE2")
        }.transition(States.STATE2, {
            println("Transitioning from STATE2 to STATE3")
            true
        }, 0.0).state(States.STATE3).onEnter(
            States.STATE3
        ) {
            println("Entering STATE3")
        }.transition(States.STATE3, {
            println("Transitioning from STATE3 to STOP")
            true
        }, 0.0).stopRunning(States.STOP)

        val srsmb = builder.build()
        srsmb.start()
        assertTrue(srsmb.isRunning)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STATE2, srsmb.currentState)
        assertTrue(srsmb.update())
        assertEquals(States.STATE3, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STOP, srsmb.currentState)
        assertTrue(srsmb.update())
        assertFalse(srsmb.update())
        assertFalse(srsmb.isRunning)

        println("Comprehensive test passed")
    }

    @Test
    fun testRestartAtBeginning() {
        println("Testing restartAtBeginning")
        val builder = SequentialRunSM.Builder<States>()
        builder.state(States.STATE1).onEnter(
            States.STATE1
        ) {
            println("Entering STATE1")
        }.transition(States.STATE1, { true }, 0.0).state(States.STATE2).onEnter(
            States.STATE2
        ) {
            println("Entering STATE2")
        }.transition(States.STATE2, {
            println("Transitioning from STATE2 to STATE3")
            true
        }, 0.0).state(States.STATE3).onEnter(
            States.STATE3
        ) {
            println("Entering STATE3")
        }.transition(States.STATE3, {
            println("Transitioning from STATE3 to STOP")
            true
        }, 0.0).stopRunning(States.STOP)

        val srsmb = builder.build()
        srsmb.start()
        assertTrue(srsmb.isRunning)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STATE2, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STATE3, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STOP, srsmb.currentState)
        assertFalse(srsmb.isRunning)

        // Restart the state machine
        srsmb.restartAtBeginning()
        println(srsmb.sustainStates)
        srsmb.start()
        assertTrue(srsmb.isRunning)
        assertEquals(States.STATE1, srsmb.currentState)
        assertTrue(srsmb.isRunning)
        assertTrue(srsmb.update())
        assertEquals(States.STATE2, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STATE3, srsmb.currentState)
        assertTrue(srsmb.update())
        assertTrue(srsmb.update())
        assertEquals(States.STOP, srsmb.currentState)
        assertTrue(srsmb.update())
        assertFalse(srsmb.update())
        assertFalse(srsmb.isRunning)

        println("Restart at beginning tested successfully")
    }

    @Test
    fun testStateMachineRunning() {
        println("Testing state machine running status")
        val builder = SequentialRunSM.Builder<States>()
        builder.state(States.STATE1).onEnter(
            States.STATE1
        ) {
            println("Entering STATE1")
        }.transition(States.STATE1, { true }, 0.0).state(States.STATE2).onEnter(
            States.STATE2
        ) {
            println("Entering STATE2")
        }.transition(States.STATE2, { true }, 0.0).state(States.STATE3).onEnter(
            States.STATE3
        ) {
            println("Entering STATE3")
        }.transition(States.STATE3, { true }, 0.0).stopRunning(States.STOP)

        val stateMachine = builder.build()
        stateMachine.start()
        assertTrue(stateMachine.isRunning)
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        assertEquals(States.STATE2, stateMachine.currentState)
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        assertEquals(States.STATE3, stateMachine.currentState)
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        assertFalse(stateMachine.update())
        assertEquals(States.STOP, stateMachine.currentState)
        assertFalse(stateMachine.isRunning)
    }

    @Test
    @Throws(InterruptedException::class)
    fun testStateTransitionWithDelay() {
        println("Testing state transition with delay")
        val builder = SequentialRunSM.Builder<States>()
        builder.state(States.STATE1).onEnter(
            States.STATE1
        ) {
            println("Entering STATE1")
        }.transition(States.STATE1, { true }, 2.0) // 2 seconds delay
            .state(States.STATE2).onEnter(
                States.STATE2
            ) {
                println("Entering STATE2")
            }.transition(States.STATE2, { true }, 0.0)
            .stopRunning(States.STOP)

        val stateMachine = builder.build()
        stateMachine.start()
        assertTrue(stateMachine.isRunning)
        assertEquals(States.STATE1, stateMachine.currentState)

        // First update should not transition due to delay
        assertTrue(stateMachine.update())
        assertEquals(States.STATE1, stateMachine.currentState)

        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        // Wait for more than the delay time
        Thread.sleep(2500) // 2.5 seconds

        // Next update should transition to STATE2
        assertTrue(stateMachine.update())
        assertEquals(States.STATE2, stateMachine.currentState)
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())

        // Final update should transition to STOP
        assertTrue(stateMachine.update())
        assertEquals(States.STOP, stateMachine.currentState)
        assertFalse(stateMachine.isRunning)
    }
}