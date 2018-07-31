package commands

import interfaces.CommandNotFoundException
import interfaces.ICommandDispatcher

class CommandDispatcher : ICommandDispatcher {
    override val commands: Map<String, () -> AbstractCommand> by lazy { populateCommands() }

    fun populateCommands(): Map<String, () -> AbstractCommand> {
        var commands: Map<String, () -> AbstractCommand> = emptyMap()

        val commandsList = listOf(
                ::HelpCommand,
                ::EchoCommand,
                ::PingCommand,
                ::SetPasswordCommand,
                ::WhoAmICommand
        )

        for (c in commandsList) {
            val name = c().name
            check(name !in commands)
            commands += name to c
        }

        return commands
    }

    override fun resolve(argv: List<String>): AbstractCommand {
        val name = argv[0]
        val constructor = commands.get(name) ?: throw CommandNotFoundException(name)
        val command = constructor()
        command.parseArguments(argv)
        command.output.setSuccess(false)
        return command
    }
}

