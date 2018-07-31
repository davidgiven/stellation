package runtime.shared

import interfaces.IClientInterface
import interfaces.ICommand
import interfaces.ICommandDispatcher
import utils.injection

class LocalClientInterface : IClientInterface {
    private val commandDispatcher by injection<ICommandDispatcher>()

    override suspend fun executeCommand(command: ICommand) {
        command.serverRun()
    }
}