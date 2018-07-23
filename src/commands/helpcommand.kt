package commands

import utils.GetoptCallback

class HelpCommand : AbstractClientCommand() {
    override val name = "help"
    override val description = "accesses the help system"

    override val options: Map<String, GetoptCallback> = emptyMap()
}
