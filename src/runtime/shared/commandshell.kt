package runtime.shared

import interfaces.CommandExecutionException
import interfaces.ICommandDispatcher
import interfaces.IConsole
import utils.get

class CommandShell(val commandDispatcher: ICommandDispatcher = get(), val console: IConsole = get()) {
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