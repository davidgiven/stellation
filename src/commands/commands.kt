package commands

import interfaces.CommandNotFoundException
import interfaces.CommandSyntaxException
import interfaces.ICommandDispatcher
import utils.CommandLineParseException
import utils.argify

class CommandDispatcher : ICommandDispatcher {
    override val commands: Map<String, () -> AbstractCommand> by lazy { populateCommands() }

    fun populateCommands(): Map<String, () -> AbstractCommand> {
        var commands: Map<String, () -> AbstractCommand> = emptyMap()

        fun addCommand(c: () -> AbstractCommand) {
            val name = c().name
            commands += name to c
        }

        addCommand(::HelpCommand)
        addCommand(::EchoCommand)
        addCommand(::PingCommand)

        return commands
    }

    override fun resolve(cmdline: String): AbstractCommand {
        try {
            return resolve(argify(cmdline))
        } catch (e: CommandLineParseException) {
            throw CommandSyntaxException(e.message ?: "syntax error")
        }
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

