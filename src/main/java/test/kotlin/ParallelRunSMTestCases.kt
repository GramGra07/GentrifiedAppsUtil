package test.kotlin

import org.firstinspires.ftc.robotcore.internal.system.Assert.assertFalse
import org.firstinspires.ftc.robotcore.internal.system.Assert.assertTrue
import org.gentrifiedApps.gentrifiedAppsUtil.stateMachine.ParallelRunSM
import org.junit.Assert.assertEquals
import org.junit.Test

class ParallelRunSMTestCases {
    internal enum class States {
        STATE1,
        STATE2,
        STATE3,
        STATE4,
        STOP
    }

    @Test
    fun testParallelRunSM() {
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(
                States.STATE1
            ) { println("Entering STATE1") }
            .state(States.STATE2)
            .onEnter(
                States.STATE2
            ) { println("Entering STATE2") }
            .state(States.STATE3)
            .onEnter(
                States.STATE3
            ) { println("Entering STATE3") }
            .stopRunning(States.STOP) { true }
        val stateMachine = builder.build(false, 100)
        stateMachine.start()
        assertTrue(stateMachine.isStarted)
        assertTrue(stateMachine.update())
        assertFalse(stateMachine.isRunning)
    }

    @Test
    fun testParallelness() {
        val map = booleanArrayOf(false, false, false)
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(States.STATE1) {
                println("Entering STATE1")
                map[0] = true
            }
            .state(States.STATE2)
            .onEnter(States.STATE2) {
                println("Entering STATE2")
                map[1] = true
            }
            .state(States.STATE3)
            .onEnter(States.STATE3) {
                println("Entering STATE3")
                map[2] = true
            }
            .stopRunning(States.STOP) { true }
        val stateMachine = builder.build(false, 100)
        stateMachine.start()
        assertTrue(stateMachine.isStarted)
        assertTrue(stateMachine.update())
        assertTrue(map[0])
        assertTrue(map[1])
        assertTrue(map[2])
        assertFalse(stateMachine.isRunning)
    }

    //Test for the stopRunning method
    @Test
    @Throws(InterruptedException::class)
    fun testStopRunning() {
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(
                States.STATE1
            ) { println("Entering STATE1") }
            .state(States.STATE2)
            .onEnter(
                States.STATE2
            ) { println("Entering STATE2") }
            .state(States.STATE3)
            .onEnter(
                States.STATE3
            ) { println("Entering STATE3") }
            .stopRunning(States.STOP) { false }
        val stateMachine = builder.build(true, 5000)
        stateMachine.start()
        assertTrue(stateMachine.isStarted)
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        assertFalse(stateMachine.checkExitTransition())
        assertTrue(stateMachine.isRunning)
        Thread.sleep(5100)
        assertTrue(stateMachine.update())
        assertFalse(stateMachine.isRunning)
    }

    @Test
    @Throws(InterruptedException::class)
    fun testWithoutTimeout() {
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(
                States.STATE1
            ) { println("Entering STATE1") }
            .state(States.STATE2)
            .onEnter(
                States.STATE2
            ) { println("Entering STATE2") }
            .state(States.STATE3)
            .onEnter(
                States.STATE3
            ) { println("Entering STATE3") }
            .stopRunning(States.STOP) { false }
        val stateMachine = builder.build(false, 1000)
        stateMachine.start()
        assertTrue(stateMachine.isStarted)
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.update())
        assertFalse(stateMachine.checkExitTransition())
        assertTrue(stateMachine.isRunning)
        Thread.sleep(1100)
        assertTrue(stateMachine.update())
        assertTrue(stateMachine.isRunning)
    }

    @Test
    fun testStop() {
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(
                States.STATE1
            ) { println("Entering STATE1") }
            .state(States.STATE2)
            .onEnter(
                States.STATE2
            ) { println("Entering STATE2") }
            .state(States.STATE3)
            .onEnter(
                States.STATE3
            ) { println("Entering STATE3") }
            .stopRunning(States.STOP) { true }
        val stateMachine = builder.build(false, 100)
        stateMachine.start()
        assertTrue(stateMachine.isStarted)
        stateMachine.stop()
        assertFalse(stateMachine.isRunning)
    }

    //test comprehensively
    @Test
    fun testComprehensive() {
        val map = booleanArrayOf(false, false, false)
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(States.STATE1) {
                println("Entering STATE1")
                map[0] = true
            }
            .state(States.STATE2)
            .onEnter(States.STATE2) {
                println("Entering STATE2")
                map[1] = true
            }
            .state(States.STATE3)
            .onEnter(States.STATE3) {
                println("Entering STATE3")
                map[2] = true
            }
            .stopRunning(States.STOP) { true }
        val stateMachine = builder.build(false, 100)
        stateMachine.start()
        assertTrue(stateMachine.isStarted)
        assertTrue(stateMachine.update())
        assertTrue(map[0])
        assertTrue(map[1])
        assertTrue(map[2])
        assertFalse(stateMachine.isRunning)
    }

    @Test
    fun testTiming() {
        val map = booleanArrayOf(false, false, false)
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(States.STATE1) {
                println("Entering STATE1")
                map[0] = true
                // delay
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            .state(States.STATE2)
            .onEnter(States.STATE2) {
                println("Entering STATE2")
                map[1] = true
                // delay
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            .state(States.STATE3)
            .onEnter(States.STATE3) {
                println("Entering STATE3")
                map[2] = true
                // delay
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            .stopRunning(States.STOP) { true }
        val stateMachine = builder.build(false, 100)
        stateMachine.start()
        assertTrue(stateMachine.isStarted)
        assertTrue(stateMachine.update())
        assertTrue(map[0])
        assertTrue(map[1])
        assertTrue(map[2])
        assertFalse(stateMachine.isRunning)
    }

    @Test
    fun testStopRunningTiming() {
        val map = booleanArrayOf(false, false, false)
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(States.STATE1) {
                println("Entering STATE1")
                map[0] = true
                // delay
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            .state(States.STATE2)
            .onEnter(States.STATE2) {
                println("Entering STATE2")
                map[1] = true
                // delay
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            .state(States.STATE3)
            .onEnter(States.STATE3) {
                println("Entering STATE3")
                map[2] = true
                // delay
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            .stopRunning(States.STOP) { false }
        val stateMachine = builder.build(false, 100)
        stateMachine.start()
        assertTrue(stateMachine.isStarted)
        assertTrue(stateMachine.update())
        assertTrue(map[0])
        assertTrue(map[1])
        assertTrue(map[2])
        assertTrue(stateMachine.isRunning)
    }

    @Test
    fun testStopRunningTiming2() {
        val map = booleanArrayOf(false, false, false)
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(States.STATE1) {
                println("Entering STATE1")
                map[0] = true
            }
            .state(States.STATE2)
            .onEnter(States.STATE2) {
                println("Entering STATE2")
                map[1] = true
            }
            .state(States.STATE3)
            .onEnter(States.STATE3) {
                println("Entering STATE3")
                map[2] = true
            }
            .state(States.STATE4)
            .onEnter(States.STATE4) {
                println("Entering STATE4")
                map[2] = false
            }
            .stopRunning(States.STOP) { true }
        val stateMachine = builder.build(false, 100)
        stateMachine.start()
        assertTrue(stateMachine.isStarted)
        assertTrue(stateMachine.update())
        assertTrue(map[0])
        assertTrue(map[1])
        assertFalse(map[2])
        assertFalse(stateMachine.isRunning)
    }

    // mismatch states and onenters
    @Test
    fun testMismatchStatesAndOnEnters() {
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(
                States.STATE1
            ) { println("Entering STATE1") }
            .state(States.STATE2)
            .stopRunning(States.STOP) { true }
        try {
            val stateMachine = builder.build(false, 100)
        } catch (e: IllegalArgumentException) {
            assertEquals("Not all states have corresponding onEnter commands", e.message)
        }
    }

    // test a negative timeout
    @Test
    fun testNegativeTimeout() {
        val builder = ParallelRunSM.Builder<States>()
        builder.state(States.STATE1)
            .onEnter(
                States.STATE1
            ) { println("Entering STATE1") }
            .state(States.STATE2)
            .onEnter(
                States.STATE2
            ) { println("Entering STATE2") }
            .state(States.STATE3)
            .onEnter(
                States.STATE3
            ) { println("Entering STATE3") }
            .stopRunning(States.STOP) { true }
        try {
            val stateMachine = builder.build(false, -100)
        } catch (e: IllegalArgumentException) {
            assertEquals("Timeout must be a positive integer", e.message)
        }
    }
}
