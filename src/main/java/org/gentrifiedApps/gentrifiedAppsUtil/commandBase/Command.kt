package org.gentrifiedApps.gentrifiedAppsUtil.commandBase

open class AbsCommand {
    var isInterruptible: Boolean = false
    var function: () -> Boolean
    var isDone: Boolean = false
    var isRunning: Boolean = false
    var isCancelled: Boolean = false
    fun reset() {
        isDone = false
        isRunning = false
        isCancelled = false
    }

    constructor(
        isInterruptible: Boolean, function: () -> Boolean,
    ) {
        this.isInterruptible = isInterruptible
        this.function = function
        reset()
    }

    fun execute(): Boolean {
        if (isCancelled) {
            return false
        }
        isRunning = true
        try {
            isDone = function.invoke()
            isRunning = !isDone
        } catch (_: Exception) {
            isDone = false
            isRunning = false
        }
        return isDone
    }

    fun cancel() {
        if (isInterruptible) {
            reset()
            isCancelled = true
            isDone = true
        }
    }

    companion object {
        @JvmStatic
        fun nullCommand(): AbsCommand {
            return NullCommand()
        }

        @JvmStatic
        fun interruptibleCommand(isInterruptible: Boolean, function: () -> Boolean): AbsCommand {
            return InterruptibleCommand(isInterruptible, function)
        }

        @JvmStatic
        fun interruptibleCommand(function: () -> Boolean): AbsCommand {
            return InterruptibleCommand(function)
        }

        @JvmStatic
        fun command(isInterruptible: Boolean, function: () -> Boolean): AbsCommand {
            return Command(isInterruptible, function)
        }

        @JvmStatic
        fun command(function: () -> Boolean): AbsCommand {
            return Command(function)
        }

        @JvmStatic
        fun parallelGroup(vararg commands: AbsCommand): CommandGroup {
            return ParallelGroup(*commands)
        }

        @JvmStatic
        fun parallelGroup(commands: ArrayList<AbsCommand>): CommandGroup {
            return ParallelGroup(commands)
        }

        @JvmStatic
        fun parallelGroup(): CommandGroup {
            return ParallelGroup()
        }

        @JvmStatic
        fun sequentialGroup(vararg commands: AbsCommand): CommandGroup {
            return SequentialGroup(*commands)
        }

        @JvmStatic
        fun sequentialGroup(commands: ArrayList<AbsCommand>): CommandGroup {
            return SequentialGroup(commands)
        }

        @JvmStatic
        fun sequentialGroup(): CommandGroup {
            return SequentialGroup()
        }
    }
}

class Command : AbsCommand {
    constructor(isInterruptible: Boolean, function: () -> Boolean)
            : super(isInterruptible, function)

    constructor(function: () -> Boolean) : super(false, function)
}

class NullCommand : AbsCommand {
    constructor() : super(false, { true })
}

class InterruptibleCommand : AbsCommand {
    constructor(isInterruptible: Boolean, function: () -> Boolean)
            : super(isInterruptible, function)

    constructor(function: () -> Boolean) : super(true, function)
}

