package interfaces

import utils.Message

class RemoteCommandExecutionException(e: String): CommandExecutionException("remote error: $e")

interface IServerInterface {
    suspend fun executeCommand(argv: List<String>): Message
}

