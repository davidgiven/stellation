package commands

import interfaces.CommandExecutionException
import interfaces.CommandSyntaxException
import interfaces.ICommand
import interfaces.IConsole
import model.Model
import utils.GetoptException
import utils.Message
import utils.getopt
import utils.injection
import utils.setSuccess

class NotAServerCommandException : CommandExecutionException("this command can't be used on the server")

abstract class AbstractCommand : ICommand {
    protected val model by injection<Model>()
    protected val console by injection<IConsole>()

    override lateinit var argv: List<String>
    override var input = Message()
    override var output = Message()

    override fun parseArguments(argv: List<String>) {
        this.argv = argv
        if (options.isNotEmpty()) {
            try {
                getopt(argv.drop(1), options)
            } catch (e: GetoptException) {
                throw CommandSyntaxException(e.message ?: "syntax error")
            }
        }
    }

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

