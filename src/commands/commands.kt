package commands

import interfaces.IConsole
import utils.CommandLineParseException
import utils.argify
import utils.get
import utils.setSuccess

open class CommandExecutionException(s: String, e: Throwable? = null) : Exception(s, e)
class CommandNotFoundException(name: String) : CommandExecutionException("command '$name' not found")
class CommandSyntaxException(message: String) : CommandExecutionException(message)

class CommandDispatcher {
    var allCommands: Map<String, () -> AbstractCommand> = emptyMap()
    var localCommands: Map<String, () -> AbstractLocalCommand> = emptyMap()
    var clientCommands: Map<String, () -> AbstractClientCommand> = emptyMap()
    var remoteCommands: Map<String, () -> AbstractRemoteCommand> = emptyMap()

    init {
        addLocalCommand(::HelpCommand)
        addLocalCommand(::EchoCommand)
        addRemoteCommand(::PingCommand)
    }

    private fun addClientCommand(c: () -> AbstractClientCommand) {
        val p = c().name to c

        allCommands += p
        clientCommands += p
    }

    private fun addRemoteCommand(c: () -> AbstractRemoteCommand) {
        val name = c().name

        allCommands += name to c
        remoteCommands += name to c
    }

    private fun addLocalCommand(c: () -> AbstractLocalCommand) {
        val name = c().name

        allCommands += name to c
        localCommands += name to c
    }

    private fun findCommand(argv: List<String>): AbstractCommand {
        val name = argv[0]
        val constructor = allCommands.get(name) ?: throw CommandNotFoundException(name)
        val command = constructor()
        command.parseArguments(argv)
        return command
    }

    private fun safeArgifu(arg: String): List<String> =
            try {
                argify(arg)
            } catch (e: CommandLineParseException) {
                throw CommandSyntaxException(e.message ?: "syntax error")
            }

    suspend fun callFromClient(arg: String) {
        val console: IConsole = get()
        try {
            val argv = safeArgifu(arg)
            if (argv.isNotEmpty()) {
                val command = findCommand(argv)

                command.output.setSuccess(false)
                command.run()
                command.renderResult()
            }
        } catch (e: CommandExecutionException) {
            console.println("Failed: ${e.message ?: "unknown reason"}")
        } catch (e: Throwable) {
            console.println("Internal client error:")
            console.println(e.toString())
            throw e
        }
    }
}

