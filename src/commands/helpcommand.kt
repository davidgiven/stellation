package commands

import interfaces.ICommandDispatcher
import utils.injection

class HelpCommand : AbstractLocalCommand() {
    val commandDispatcher by injection<ICommandDispatcher>()

    override val name = "help"
    override val description = "accesses the help system"

    override suspend fun renderResult() {
        console.println("The following commands are supported:")
        val names = commandDispatcher.commands.keys.toList().sorted()
        for (name in names) {
            val command = commandDispatcher.commands[name]!!()
            console.println("  $name: ${command.description}")
        }
    }
}
