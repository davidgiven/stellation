package runtime.shared

import interfaces.IClientInterface
import interfaces.ICommand
import interfaces.ICommandDispatcher
import interfaces.UNIMPLEMENTED
import utils.injection

class LocalClientInterface : IClientInterface {
    private val commandDispatcher by injection<ICommandDispatcher>()

    override fun setCredentials(username: String, password: String) = UNIMPLEMENTED()

    override suspend fun executeCommand(command: ICommand) {
        command.serverRun()
    }
}