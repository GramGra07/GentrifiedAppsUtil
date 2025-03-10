package test.kotlin.classes

import junit.framework.TestCase
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Timeout
import org.junit.Test

class TimeoutTest {
    @Test
    fun testTimeout() {
        val timeout = Timeout(1.0, { false })
        timeout.start()
        Thread.sleep(1000)
        TestCase.assertTrue(timeout.update())
        TestCase.assertTrue(timeout.isTimedOut())
    }
    @Test
    fun testTimeoutBreak() {
        val timeout = Timeout(1.0, { true })
        timeout.start()
        TestCase.assertTrue(timeout.update())
        TestCase.assertTrue(timeout.isTimedOut())
    }
}