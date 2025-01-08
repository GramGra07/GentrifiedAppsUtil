package org.gentrifiedApps.gentrifiedAppsUtil

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito


class LoopTimeAnalyzerTest {
    var test = 1
    private var initLoop: Runnable = Runnable { test++ }
    private var function: Runnable = Runnable { println("TestFunction") }
    val testableFunction = TestableFunctions("TestFunction", function)
    private var loopTimeAnalyzer: LoopTimeAnalyzer =
        LoopTimeAnalyzer(10.0, initLoop, listOf(testableFunction))


    @Test
    fun testInit() {
        loopTimeAnalyzer.init()
        assertEquals(2, test)
    }

    @Test
    fun testTestEach() {
        loopTimeAnalyzer.testEach()
    }

    @Test
    fun `init should call initLoop`() {
        // Arrange
        val initLoop = Mockito.mock(Runnable::class.java)
        val loopAnalyzer = LoopTimeAnalyzer(10.0, initLoop, emptyList())

        // Act
        loopAnalyzer.init()

        // Assert
        Mockito.verify(initLoop).run()
    }

    @Test
    fun `mainLoop should update LoopTimeController`() {
        // Arrange
        val loopAnalyzer = LoopTimeAnalyzer(10.0, Runnable {}, emptyList())
        val loopTimeController = Mockito.spy(LoopTimeController())
        loopAnalyzer.loopTimeController = loopTimeController

        // Act
        loopAnalyzer.mainLoop()

        // Assert
        Mockito.verify(loopTimeController).update()
    }

    @Test
    fun `testEach should iterate functions and analyze times`() {
        // Arrange
        val mockFunction1 = Mockito.mock(Runnable::class.java)
        val testableFunction1 = TestableFunctions("Function1", mockFunction1)
        val mockFunction2 = Mockito.mock(Runnable::class.java)
        val testableFunction2 = TestableFunctions("Function2", mockFunction2)

        val loopAnalyzer = LoopTimeAnalyzer(
            testingLoops = 5.0,
            initLoop = Runnable {},
            allFunctions = listOf(testableFunction1, testableFunction2)
        )

        val loopTimeController = Mockito.spy(LoopTimeController())
        loopAnalyzer.loopTimeController = loopTimeController

        // Act
        loopAnalyzer.testEach()

        // Assert
        assertEquals(5, testableFunction1.allTimes.size)
        assertEquals(5, testableFunction2.allTimes.size)
        assertTrue(testableFunction1.averageTime > 0.0)
        assertTrue(testableFunction2.averageTime > 0.0)
        Mockito.verify(mockFunction1, Mockito.times(5)).run()
        Mockito.verify(mockFunction2, Mockito.times(5)).run()
    }

    @Test
    fun `testEach should accurately iterate through functions and calculate times`() {
        val testableFunction1 = TestableFunctions("Function1", Runnable { Thread.sleep(10) })
        val testableFunction2 = TestableFunctions("Function2", Runnable { Thread.sleep(20) })
        // Inject the custom LoopTimeController into the LoopTimeAnalyzer
        val loopAnalyzer = LoopTimeAnalyzer(
            testingLoops = 3.0,
            initLoop = Runnable {},
            allFunctions = listOf(testableFunction1, testableFunction2),
        )

        // Execute the testEach function
        loopAnalyzer.testEach()

        // Assertions for Function1
        assertEquals(3, testableFunction1.allTimes.size)
        assertEquals(30.0, testableFunction1.allTimes.sum(), 10.0)
        assertEquals(9.0, testableFunction1.averageTime, 4.0)

        // Assertions for Function2
        assertEquals(3, testableFunction2.allTimes.size)
        assertEquals(60.0, testableFunction2.allTimes.sum(), 20.0)
        assertEquals(20.0, testableFunction2.averageTime, 8.0)
    }


    @Test
    fun `testEach should not run functions when testingLoops is zero`() {
        val testableFunction = TestableFunctions("NoLoopsFunction", Runnable { })
        val loopAnalyzer = LoopTimeAnalyzer(
            testingLoops = 0.0,
            initLoop = Runnable {},
            allFunctions = listOf(testableFunction)
        )

        loopAnalyzer.testEach()

        assertTrue(testableFunction.allTimes.isEmpty())
        assertEquals(0, testableFunction.averageTime.toInt())
    }

    @Test
    fun `init should execute the provided initLoop`() {
        var wasInitCalled = false
        val initLoop = Runnable { wasInitCalled = true }
        val loopAnalyzer = LoopTimeAnalyzer(10.0, initLoop, emptyList())

        loopAnalyzer.init()

        assertTrue(wasInitCalled)
    }

    @Test
    fun `testEach should integrate LoopTimeController updates with TestableFunctions`() {
        val testableFunction = TestableFunctions("IntegrationFunction", Runnable { })

        val loopAnalyzer = LoopTimeAnalyzer(
            testingLoops = 5.0,
            initLoop = Runnable {},
            allFunctions = listOf(testableFunction),
        )

        loopAnalyzer.testEach()

        assertEquals(2.0, testableFunction.averageTime, 4.0)
    }

    @Test
    fun `testEach should handle empty list of functions`() {
        val loopAnalyzer = LoopTimeAnalyzer(
            testingLoops = 5.0,
            initLoop = Runnable {},
            allFunctions = emptyList()
        )

        loopAnalyzer.testEach()

        // No exception should be thrown
    }
}

class TestableFunctionsTest {

    @Test
    fun `run should execute the provided function`() {
        // Arrange
        val mockFunction = Mockito.mock(Runnable::class.java)
        val testableFunction = TestableFunctions("TestFunction", mockFunction)

        // Act
        testableFunction.run()

        // Assert
        Mockito.verify(mockFunction).run()
    }

    @Test
    fun `addTime should append time to allTimes`() {
        // Arrange
        val testableFunction = TestableFunctions("TestFunction", Runnable {})
        val time = 5.0

        // Act
        testableFunction.addTime(time)

        // Assert
        assertEquals(1, testableFunction.allTimes.size)
        assertEquals(time.toInt(), testableFunction.allTimes[0].toInt())
    }

    @Test
    fun `analyze should calculate average time`() {
        // Arrange
        val testableFunction = TestableFunctions("TestFunction", Runnable {})
        testableFunction.addTime(2.0)
        testableFunction.addTime(4.0)

        // Act
        testableFunction.analyze()

        // Assert
        assertEquals(3, testableFunction.averageTime.toInt())
    }
}