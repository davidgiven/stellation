package commands

import interfaces.CommandExecutionException
import interfaces.CommandMessage
import interfaces.CommandSyntaxException
import interfaces.ICommand
import interfaces.IConsole
import model.Model
import utils.Flags
import utils.GetoptException
import utils.getopt
import utils.injection

class NotAServerCommandException : CommandExecutionException("this command can't be used on the server")

abstract class AbstractCommand : ICommand {
    protected val model by injection<Model>()
    protected val console by injection<IConsole>()

    override lateinit var argv: List<String>
    override val flags = Flags()
    override var input = CommandMessage()
    override var output = CommandMessage()

    override fun parseArguments(argv: List<String>) {
        this.argv = argv
        try {
            val remaining = getopt(argv.drop(1), flags)
            parseRemainingArguments(remaining)
        } catch (e: GetoptException) {
            throw CommandSyntaxException(e.message ?: "syntax error")
        }
        validateArguments()
    }

    override fun parseRemainingArguments(argv: List<String>) {
        if (argv.isNotEmpty()) {
            throw CommandSyntaxException("unrecognised arguments starting with '${argv.first()}'")
        }
    }

    override fun validateArguments() {}

    override suspend fun run() {
        output.setSuccess(true)
    }

    override fun serverRun() {
        throw NotAServerCommandException()
    }

    override suspend fun renderResult() {
        console.println("OK")
    }
}

