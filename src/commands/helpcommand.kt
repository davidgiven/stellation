package commands

import utils.GetoptCallback
import utils.get
import utils.setSuccess

class HelpCommand : AbstractLocalCommand() {
    override val name = "help"
    override val description = "accesses the help system"

    override val options: Map<String, GetoptCallback> = emptyMap()

    override suspend fun run() {
        output.setSuccess(true)
    }

    override suspend fun renderResult() {
        console.println("The following commands are supported:")
        val commandDispatcher: CommandDispatcher = get()
        val names = commandDispatcher.allCommands.keys.toList().sorted()
        for (name in names) {
            val command = commandDispatcher.allCommands[name]!!()
            console.println("  $name: ${command.description}")
        }
    }
}
