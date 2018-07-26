package runtime.shared

import interfaces.CommandMessage
import interfaces.ICommandDispatcher
import interfaces.IClientInterface
import utils.injection

class LocalClientInterface : IClientInterface {
    private val commandDispatcher by injection<ICommandDispatcher>()

    override suspend fun executeCommand(argv: List<String>): CommandMessage {
        val command = commandDispatcher.resolve(argv)
        command.run()
        return command.output
    }
}