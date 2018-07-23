package commands

import utils.GetoptCallback

class HelpCommand : AbstractClientCommand() {
    override val name = "help"
    override val description = "accesses the help system"

    override val options: Map<String, GetoptCallback> = emptyMap()

    override suspend fun run() {
        output.setSuccess(true)
    }

    override suspend fun renderResult() {
        console.println("This is the help!")
    }
}
