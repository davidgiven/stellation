package commands

import interfaces.ICommandDispatcher
import utils.GetoptCallback
import utils.get

class HelpCommand : AbstractLocalCommand() {
    val commandDispatcher by lazy { get<ICommandDispatcher>() }

    override val name = "help"
    override val description = "accesses the help system"

    override val options: Map<String, GetoptCallback> = emptyMap()

    override suspend fun renderResult() {
        console.println("The following commands are supported:")
        val names = commandDispatcher.commands.keys.toList().sorted()
        for (name in names) {
            val command = commandDispatcher.commands[name]!!()
            console.println("  $name: ${command.description}")
        }
    }
}
