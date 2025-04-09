package test.kotlin.classes

import junit.framework.TestCase
import org.gentrifiedApps.gentrifiedAppsUtil.controllers.driverAid.DriverAid
import org.junit.Test
import java.util.function.BooleanSupplier

public class DATest {
    enum class TestState {
        IDLE, ACTIVE
    }
    @Test
    fun testDriverAidInitialization() {
        val driverAid = DriverAid(TestState::class.java)
        TestCase.assertNotNull(driverAid)
    }

    @Test
    fun testDAFuncRunInit() {
        val driverAid = DriverAid(TestState::class.java)
        val funcs = Runnable { println("Running funcs") }
        val runConstant = Runnable { println("Running constant") }
        val isEnded = BooleanSupplier { false }
        val resetOnInit = Runnable { println("Reset on init") }

        val daFunc = DriverAid.DAFunc(
            driverAid,
                state = TestState.IDLE,
                funcs = funcs,
                runConstant = runConstant,
                isEnded = isEnded,
                resetOnInit = resetOnInit
        )
        daFunc.runInit()

        driverAid.update()
        TestCase.assertEquals(TestState.IDLE, driverAid.daState)
        TestCase.assertNotNull(driverAid.getDAFunc())
    }

    @Test
    fun testDAFuncRunALot() {
        val driverAid = DriverAid(TestState::class.java)
        val funcs = Runnable { println("Running funcs") }
        val runConstant = Runnable { println("Running constant") }
        val isEnded = BooleanSupplier { false }
        val resetOnInit = Runnable { println("Reset on init") }

        val daFunc = DriverAid.DAFunc(
            driverAid,
                state = TestState.IDLE,
                funcs = funcs,
                runConstant = runConstant,
                isEnded = isEnded,
                resetOnInit = resetOnInit
        )
        daFunc.runInit()

        driverAid.update()
        TestCase.assertFalse(daFunc.isEnded())
        TestCase.assertTrue(true) // Just to ensure the test runs without exceptions
    }
    // test multiple functions

    @Test
    fun testDriverAidUpdate() {
        val driverAid = DriverAid(TestState::class.java)
        val funcs = Runnable { println("Running funcs") }
        val runConstant = Runnable { println("Running constant") }
        val isEnded = BooleanSupplier { false }
        val resetOnInit = Runnable { println("Reset on init") }
        val daFunc = DriverAid.DAFunc(
                driverAid,
                state = TestState.IDLE,
                funcs = funcs,
                runConstant = runConstant,
                isEnded = isEnded,
                resetOnInit = resetOnInit
        )
        daFunc.runInit()
        driverAid.update()
        TestCase.assertFalse(daFunc.isEnded())
        TestCase.assertTrue(true) // Just to ensure the test runs without exceptions
        // new test

        val funcs2 = Runnable { println("Running funcs 2") }
        val runConstant2 = Runnable { println("Running constant 2") }
        val isEnded2 = BooleanSupplier { false }
        val resetOnInit2 = Runnable { println("Reset on init 2") }
        val daFunc2 = DriverAid.DAFunc(
                driverAid,
                state = TestState.IDLE,
                funcs = funcs2,
                runConstant = runConstant2,
                isEnded = isEnded2,
                resetOnInit = resetOnInit2
        )
        daFunc2.runInit()
        driverAid.update()
        TestCase.assertFalse(daFunc2.isEnded())
        TestCase.assertTrue(true) // Just to ensure the test runs without exceptions
        driverAid.setDriverAidFunction(daFunc)
        driverAid.update()
        TestCase.assertFalse(daFunc.isEnded())
        TestCase.assertTrue(true) // Just to ensure the test runs without exceptions
    }
    @Test
    fun testAllAreRun(){
        var storage = BooleanArray(3, { false })
        val driverAid = DriverAid(TestState::class.java)
        val funcs = Runnable { storage[0] = true }
        val runConstant = Runnable { storage[1] = true }
        val isEnded = BooleanSupplier { storage.contentEquals(BooleanArray(3) { true }) }
        val resetOnInit = Runnable { storage[2] = true }
        val daFunc = DriverAid.DAFunc(
                driverAid,
                state = TestState.IDLE,
                funcs = funcs,
                runConstant = runConstant,
                isEnded = isEnded,
                resetOnInit = resetOnInit
        )
        daFunc.runInit()
        driverAid.update()
        println(storage.contentToString())
        TestCase.assertTrue(daFunc.isEnded())
        TestCase.assertTrue(true) // Just to ensure the test runs without exceptions
    }
}