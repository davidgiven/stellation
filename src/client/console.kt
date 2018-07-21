package client

import ui.ConsoleWindow
import ui.show
import utils.Job
import utils.get

class Console {
    val window = ConsoleWindow(get(), ::onCommand)

    fun show(): Console {
        window.show()
        return this
    }

    fun println(s: String) = window.print(s)

    fun execute(command: String) {
        window.print("> ${command}")
    }

    private fun onCommand(command: String) {
        Job {
            execute(command)
        }
    }
}