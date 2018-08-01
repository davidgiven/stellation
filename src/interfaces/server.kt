package interfaces

class RemoteCommandExecutionException(e: String) : CommandExecutionException("remote error: $e")

interface IClientInterface {
    fun setCredentials(username: String, password: String)

    suspend fun executeCommand(command: ICommand)
}

