package commands

import interfaces.IConsole
import model.Model
import utils.GetoptCallback
import utils.GetoptException
import utils.Parameters
import utils.get
import utils.getSuccess
import utils.getopt

abstract class AbstractCommand {
    protected val model: Model = get()
    protected val console: IConsole = get()

    open val needsAuthentication = true
    abstract val name: String
    abstract val description: String
    abstract val options: Map<String,GetoptCallback>

    lateinit var argv: List<String>
    var input = Parameters()
    var output = Parameters()

    open fun parseArguments(argv: List<String>) {
        this.argv = argv
        if (options.isNotEmpty()) {
            try {
                getopt(argv.drop(1), options)
            } catch (e: GetoptException) {
                throw CommandSyntaxException(e.message ?: "syntax error")
            }
        }
    }

    open suspend fun run() {
    }

    open suspend fun renderResult() {
        val success = if (output.getSuccess()) "success" else "failure"
        console.println("Command ${name} completed: ${success}.")
    }
}

