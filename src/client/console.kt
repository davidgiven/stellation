package client

import interfaces.IConsole
import ui.ConsoleWindow
import ui.show
import utils.Job
import utils.get
import commands.HelpCommand

class Console: IConsole {
    val window = ConsoleWindow(get(), ::onCommand)

    fun show(): Console {
        window.show()
        return this
    }

    override suspend fun println(s: String) = window.print(s)

    suspend fun execute(command: String) {
        window.print("> ${command}")

        val command = HelpCommand()
        command.parseArguments(emptyList())
        command.run()
        command.renderResult()
    }

    private fun onCommand(command: String) {
        Job {
            execute(command)
        }
    }
}