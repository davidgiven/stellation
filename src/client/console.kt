package client

import interfaces.IConsole
import interfaces.IUi
import runtime.shared.CommandShell
import ui.ConsoleWindow
import ui.show
import utils.Job
import utils.lazyget

class Console : IConsole {
    lateinit var window: ConsoleWindow
    val ui: IUi by lazyget()
    val commandShell: CommandShell by lazyget()

    fun show(): Console {
        window = ConsoleWindow(ui, ::onCommand)
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