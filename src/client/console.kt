package client

import interfaces.IConsole
import ui.ConsoleWindow
import ui.show
import utils.Job
import utils.get
import utils.argify
import commands.CommandDispatcher

class Console: IConsole {
    val window = ConsoleWindow(get(), ::onCommand)

    fun show(): Console {
        window.show()
        return this
    }

    override suspend fun println(message: String) = window.print(message)

    suspend fun execute(arg: String) {
        window.print("> ${arg}")

        val commandDispatcher: CommandDispatcher = get()
        commandDispatcher.callFromClient(arg)
    }

    private fun onCommand(command: String) {
        Job {
            execute(command)
        }
    }
}