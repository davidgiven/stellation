package commands

import interfaces.ICommandDispatcher
import interfaces.throwCommandNotFoundException

class CommandDispatcher : ICommandDispatcher {
    override val commands: Map<String, () -> AbstractCommand> by lazy { populateCommands() }

    fun populateCommands(): Map<String, () -> AbstractCommand> {
        var commands: Map<String, () -> AbstractCommand> = emptyMap()

        val commandsList = listOf(
                ::HelpCommand,
                ::EchoCommand,
                ::PingCommand,
                ::SetPasswordCommand,
                ::WhoAmICommand,
                ::StarsCommand,
                ::ExCommand,
                ::RenameCommand
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
        val constructor = commands.get(name) ?: throwCommandNotFoundException(name)
        val command = constructor()
        command.parseArguments(argv)
        command.output.setSuccess(false)
        return command
    }
}

