package commands

class HelpCommand : AbstractClientCommand() {
    override val name = "help"
    override val description = "accesses the help system"

    override suspend fun clientRun(argv: List<String>) {
    }
}
