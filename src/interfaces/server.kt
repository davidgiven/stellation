package interfaces

interface IClientInterface {
    fun setCredentials(username: String, password: String)

    suspend fun executeCommand(command: ICommand)
}

