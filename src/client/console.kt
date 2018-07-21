package client

import kotlinx.coroutines.experimental.launch
import ui.ConsoleWindow
import ui.show
import utils.get

class Console {
    val window = ConsoleWindow(get(), { launch { onCommand(it) } })

    fun show(): Console {
        window.show()
        return this
    }

    suspend fun onCommand(command: String) {
        window.print("Got command: ${command}")
    }
}