package test.kotlin

import org.gentrifiedApps.gentrifiedAppsUtil.looptime.analyzer.LoopTimeAnalyzer
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.LoopTimeController
import org.gentrifiedApps.gentrifiedAppsUtil.looptime.analyzer.TestableFunctions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class LoopTimeAnalyzerTest {
    var test = 1
    private var initLoop: Runnable = Runnable { test++ }
    private var function: Runnable = Runnable { println("TestFunction") }
    val testableFunction = TestableFunctions("TestFunction", function)
    private var loopTimeAnalyzer: LoopTimeAnalyzer =
        LoopTimeAnalyzer(
            null,10.0, initLoop, listOf(testableFunction))


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
    fun initShouldCallInitLoop2() {
        // Arrange
        var bool = false
        val loopAnalyzer = LoopTimeAnalyzer(null,10.0, { bool = true }, emptyList())

        // Act
        loopAnalyzer.init()

        // Assert
        assertTrue(bool)
    }

    @Test
    fun testEachShouldUpdate() {
        // Arrange
        var bool = false
        val loopAnalyzer = LoopTimeAnalyzer(null,10.0, Runnable {}, listOf(TestableFunctions("TestFunction", Runnable { bool = true })))
        val loopTimeController = LoopTimeController()
        loopAnalyzer.loopTimeController = loopTimeController

        // Act
        loopAnalyzer.testEach()

        // Assert
        assertTrue(bool)
    }

    @Test
    fun testEachShouldIterateFunctions() {
        // Arrange
        var bool1 = false
        val testableFunction1 = TestableFunctions("Function1", { bool1 = true })
        var bool2 = false
        val testableFunction2 = TestableFunctions("Function2", {bool2 = true})

        val loopAnalyzer = LoopTimeAnalyzer(
            null,
            testingLoops = 5.0,
            initLoop = Runnable {},
            allFunctions = listOf(testableFunction1, testableFunction2)
        )

        val loopTimeController = LoopTimeController()
        loopAnalyzer.loopTimeController = loopTimeController

        // Act
        loopAnalyzer.testEach()

        // Assert
        assertEquals(5, testableFunction1.allTimes.size)
        assertEquals(5, testableFunction2.allTimes.size)
        assertTrue(testableFunction1.averageTime > 0.0)
        assertTrue(testableFunction2.averageTime > 0.0)
        assertTrue(bool1)
        assertTrue(bool2)
    }

    @Test
    fun testEachShouldAccumulateTimes() {
        val testableFunction1 = TestableFunctions("Function1", Runnable { Thread.sleep(10) })
        val testableFunction2 = TestableFunctions("Function2", Runnable { Thread.sleep(20) })
        // Inject the custom LoopTimeController into the LoopTimeAnalyzer
        val loopAnalyzer = LoopTimeAnalyzer(
            null,
            testingLoops = 3.0,
            initLoop = Runnable {},
            allFunctions = listOf(testableFunction1, testableFunction2),
        )

        // Execute the testEach function
        loopAnalyzer.testEach()

        // Assertions for Function1
        assertEquals(3, testableFunction1.allTimes.size)
        assert(testableFunction1.allTimes.sum() > 20.0)

        // Assertions for Function2
        assertEquals(3, testableFunction2.allTimes.size)
        assert(testableFunction2.allTimes.sum() > 20.0)
    }

    @Test
    fun testEachShouldAccumulateTimesLong() {
        val testableFunction1 = TestableFunctions("Function1", Runnable { Thread.sleep(5000) })
        val testableFunction2 = TestableFunctions("Function2", Runnable { Thread.sleep(2000) })
        // Inject the custom LoopTimeController into the LoopTimeAnalyzer
        val loopAnalyzer = LoopTimeAnalyzer(
            null,
            testingLoops = 3.0,
            initLoop = Runnable {},
            allFunctions = listOf(testableFunction1, testableFunction2),
        )

        // Execute the testEach function
        loopAnalyzer.testEach()

        // Assertions for Function1
        assertEquals(3, testableFunction1.allTimes.size)
        assertEquals(15000.0, testableFunction1.allTimes.sum(), 5000.0)
        assertEquals(5000.0, testableFunction1.averageTime, 2000.0)

        // Assertions for Function2
        assertEquals(3, testableFunction2.allTimes.size)
        assertEquals(6000.0, testableFunction2.allTimes.sum(), 3500.0)
        assertEquals(2000.0, testableFunction2.averageTime, 1500.0)
    }

    @Test
    fun testEachShouldNotThrowException2() {
        val testableFunction = TestableFunctions("NoLoopsFunction", Runnable { })
        val loopAnalyzer = LoopTimeAnalyzer(
            null,
            testingLoops = 0.0,
            initLoop = Runnable {},
            allFunctions = listOf(testableFunction)
        )

        loopAnalyzer.testEach()

        assertTrue(testableFunction.allTimes.isEmpty())
        assertEquals(0, testableFunction.averageTime.toInt())
    }

    @Test
    fun initShouldCallInitLoop() {
        var wasInitCalled = false
        val initLoop = Runnable { wasInitCalled = true }
        val loopAnalyzer = LoopTimeAnalyzer(
            null,10.0, initLoop, emptyList())

        loopAnalyzer.init()

        assertTrue(wasInitCalled)
    }

    @Test
    fun testEachShouldRunFunctions() {
        val testableFunction = TestableFunctions("IntegrationFunction", Runnable { })

        val loopAnalyzer = LoopTimeAnalyzer(
            null,
            testingLoops = 5.0,
            initLoop = Runnable {},
            allFunctions = listOf(testableFunction),
        )

        loopAnalyzer.testEach()

        assertEquals(2.0, testableFunction.averageTime, 4.0)
    }

    @Test
    fun testEachShouldNotThrowException() {
        val loopAnalyzer = LoopTimeAnalyzer(
            null,
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
    fun runShouldCallFunction() {
        // Arrange
        var bool = false
        val testableFunction = TestableFunctions("TestFunction", { bool = true })

        // Act
        testableFunction.run()

        // Assert
        assertTrue(bool)
    }

    @Test
    fun addTimeShouldAddTimeToList() {
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
    fun analyzeShouldCalculateAverageTime() {
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