package runtime.shared

import interfaces.CommandExecutionException
import interfaces.ICommandDispatcher
import interfaces.IConsole
import utils.argify
import utils.injection
import utils.unargify

class CommandShell() {
    private val commandDispatcher by injection<ICommandDispatcher>()
    private val console by injection<IConsole>()

    suspend fun call(cmdline: String) {
        call(argify(cmdline))
    }

    suspend fun call(argv: List<String>) {
        if (argv.isEmpty()) {
            return
        }

        try {
            console.println("> ${unargify(argv)}")
            val command = commandDispatcher.resolve(argv)
            command.run()
            command.renderResult()
        } catch (e: CommandExecutionException) {
            console.println("Failed: $e")
        } catch (e: Exception) {
            console.println("Internal error: $e")
        }
    }
}