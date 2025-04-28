package test.kotlin.commandBase

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.gentrifiedApps.gentrifiedAppsUtil.commandBase.Command
import org.gentrifiedApps.gentrifiedAppsUtil.commandBase.InterruptibleCommand
import org.gentrifiedApps.gentrifiedAppsUtil.commandBase.NullCommand
import org.gentrifiedApps.gentrifiedAppsUtil.commandBase.ParallelGroup
import org.gentrifiedApps.gentrifiedAppsUtil.commandBase.SequentialGroup
import org.junit.Test

class CommandBaseTest {

    @Test
    fun testCommandExecution() {
        val command = Command { true }
        assertFalse(command.isDone)
        assertTrue(command.execute())
        assertTrue(command.isDone)
    }

    @Test
    fun testInterruptibleCommandCancellation() {
        val command = InterruptibleCommand { false }
        assertFalse(command.isCancelled)
        command.cancel()
        assertTrue(command.isCancelled)
        assertTrue(command.isDone)
    }

    @Test
    fun testNullCommand() {
        val command = NullCommand()
        assertTrue(command.execute())
        assertTrue(command.isDone)
    }

    @Test
    fun testParallelGroupExecution() {
        val command1 = Command { true }
        val command2 = Command { true }
        val group = ParallelGroup(command1, command2)
        group.run()
        assertTrue(command1.isDone)
        assertTrue(command2.isDone)
    }

    @Test
    fun testSequentialGroupExecution() {
        var step = 0
        val command1 = Command { step++ == 0 }
        val command2 = Command { step++ == 1 }
        val group = SequentialGroup(command1, command2)
        group.run()
        assertTrue(command1.isDone)
        assertFalse(command2.isDone)
        group.run()
        assertTrue(command2.isDone)
    }

    @Test
    fun testResetCommand() {
        val command = Command { true }
        command.execute()
        assertTrue(command.isDone)
        command.reset()
        assertFalse(command.isDone)
        assertFalse(command.isRunning)
        assertFalse(command.isCancelled)
    }
}