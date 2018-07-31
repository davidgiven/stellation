package server

import interfaces.IConsole

class ServerConsole: IConsole {
    override suspend fun println(message: String) {
        kotlin.io.println(message)
    }
}
