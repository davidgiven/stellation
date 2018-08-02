package commands

import interfaces.CommandMessage
import interfaces.ICommand
import interfaces.IConsole
import interfaces.throwCommandSyntaxException
import model.Model
import utils.Fault
import utils.FaultDomain.SYNTAX
import utils.Flags
import utils.getopt
import utils.injection

fun throwNotAServerCommandException(): Nothing =
        throw Fault(SYNTAX).withDetail("this command can't be used on the server")

abstract class AbstractCommand : ICommand {
    protected val model by injection<Model>()
    protected val console by injection<IConsole>()

    override lateinit var argv: List<String>
    override val flags = Flags()
    override var input = CommandMessage()
    override var output = CommandMessage()

    override fun parseArguments(argv: List<String>) {
        this.argv = argv
        val remaining = getopt(argv.drop(1), flags)
        parseRemainingArguments(remaining)
        validateArguments()
    }

    override fun parseRemainingArguments(argv: List<String>) {
        if (argv.isNotEmpty()) {
            throwCommandSyntaxException("unrecognised arguments starting with '${argv.first()}'")
        }
    }

    override fun validateArguments() {}

    override suspend fun run() {
        output.setSuccess(true)
    }

    override fun serverRun() {
        throwNotAServerCommandException()
    }

    override suspend fun renderResult() {
        console.println("OK")
    }
}

