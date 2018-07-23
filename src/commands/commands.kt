package commands

class CommandDispatcher {
    var allCommands: Map<String, () -> AbstractCommand> = emptyMap()
    var eitherCommands: Map<String, () -> AbstractCommand> = emptyMap()
    var clientCommands: Map<String, () -> AbstractClientCommand> = emptyMap()
    var serverCommands: Map<String, () -> AbstractServerCommand> = emptyMap()

    private fun addClientCommand(c: () -> AbstractClientCommand) {
        val p = c().name to c

        allCommands += p
        clientCommands += p
    }

    private fun addServerCommand(c: () -> AbstractServerCommand) {
        val name = c().name

        allCommands += name to c
        serverCommands += name to c
    }

    private fun addEitherCommand(c: () -> AbstractCommand) {
        val name = c().name

        allCommands += name to c
        eitherCommands += name to c
    }

    init {
        addClientCommand(::HelpCommand)
    }
}

