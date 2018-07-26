package interfaces

class RemoteCommandExecutionException(e: String) : CommandExecutionException("remote error: $e")

interface IClientInterface {
    suspend fun executeCommand(argv: List<String>): CommandMessage
}

