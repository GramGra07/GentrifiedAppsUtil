package org.gentrifiedApps.gentrifiedAppsUtil.classes

import org.gentrifiedApps.gentrifiedAppsUtil.classes.callbacks.Timeout
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class TimeoutTest {
    @Test
    fun testTimeout() {
        val timeout = Timeout(1.0, { false })
        timeout.start()
        Thread.sleep(1000)
        assertTrue(timeout.update())
        assertTrue(timeout.isTimedOut())
    }

    @Test
    fun testTimeoutBreak() {
        val timeout = Timeout(1.0, { true })
        timeout.start()
        assertTrue(timeout.update())
        assertTrue(timeout.isTimedOut())
    }
}