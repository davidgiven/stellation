package runtime.shared

import interfaces.CommandExecutionException
import interfaces.ICommandDispatcher
import interfaces.IConsole
import utils.injection

class CommandShell() {
    private val commandDispatcher by injection<ICommandDispatcher>()
    private val console by injection<IConsole>()

    suspend fun call(cmdline: String) {
        if (cmdline.isBlank()) {
            return
        }

        try {
            console.println("> $cmdline")
            val command = commandDispatcher.resolve(cmdline)
            command.run()
            command.renderResult()
        } catch (e: CommandExecutionException) {
            console.println("Failed: $e")
        } catch (e: Exception) {
            console.println("Internal error: $e")
        }
    }
}