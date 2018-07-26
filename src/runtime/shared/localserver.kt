package runtime.shared

import interfaces.CommandMessage
import interfaces.ICommandDispatcher
import interfaces.IServerInterface
import utils.injection

class LocalServerInterface : IServerInterface {
    private val commandDispatcher by injection<ICommandDispatcher>()

    override suspend fun executeCommand(argv: List<String>): CommandMessage {
        val command = commandDispatcher.resolve(argv)
        command.run()
        return command.output
    }
}