package org.gentrifiedApps.gentrifiedAppsUtil.commandBase

abstract class CommandGroup {
    var commands: ArrayList<AbsCommand> = ArrayList()
    abstract fun run()
}

class ParallelGroup : CommandGroup {
    constructor(commands: ArrayList<AbsCommand>) : super() {
        this.commands = commands
    }

    constructor(vararg commands: AbsCommand) : super() {
        this.commands = ArrayList(commands.asList())
    }

    override fun run() {
        for (command in commands) {
            command.execute()
        }
    }
}

class SequentialGroup : CommandGroup {
    constructor(commands: ArrayList<AbsCommand>) : super() {
        this.commands = commands
    }

    constructor(vararg commands: AbsCommand) : super() {
        this.commands = ArrayList(commands.asList())
    }

    var currentCommandIndex = 0
    override fun run() {
        if (currentCommandIndex >= commands.size) {
            return
        }
        val command = commands[currentCommandIndex]
        if (command.execute()) {
            currentCommandIndex++
        }
    }
}