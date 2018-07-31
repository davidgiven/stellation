package client

import interfaces.IConsole
import interfaces.IUi
import runtime.shared.CommandShell
import ui.ConsoleWindow
import ui.show
import utils.Job
import utils.injection

class Console : IConsole {
    val ui by injection<IUi>()
    val commandShell by injection<CommandShell>()

    lateinit var window: ConsoleWindow

    fun show(): Console {
        window = ConsoleWindow(::onCommand)
        window.show()
        return this
    }

    override suspend fun println(message: String) = window.print(message)

    suspend fun execute(arg: String) {
        commandShell.call(arg)
    }

    private fun onCommand(command: String) {
        Job {
            execute(command)
        }
    }
}