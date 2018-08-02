package runtime.shared

import interfaces.ICommandDispatcher
import interfaces.IConsole
import utils.Fault
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
        } catch (f: Fault) {
            console.println("Failed: ${f.message}")
        } catch (e: Exception) {
            console.println("Internal error: $e")
        }
    }
}