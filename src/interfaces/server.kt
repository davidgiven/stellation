package interfaces

class RemoteCommandExecutionException(e: String) : CommandExecutionException("remote error: $e")

interface IServerInterface {
    suspend fun executeCommand(argv: List<String>): CommandMessage
}

