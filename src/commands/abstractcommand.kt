package commands

import interfaces.CommandExecutionException
import interfaces.CommandSyntaxException
import interfaces.ICommand
import interfaces.IConsole
import model.Model
import utils.GetoptException
import utils.Parameters
import utils.getopt
import utils.lazyget
import utils.setSuccess

class NotAServerCommandException : CommandExecutionException("this command can't be used on the server")

abstract class AbstractCommand : ICommand {
    protected val model: Model by lazyget()
    protected val console: IConsole by lazyget()

    override lateinit var argv: List<String>
    override var input = Parameters()
    override var output = Parameters()

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

