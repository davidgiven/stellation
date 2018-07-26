package runtime.shared

import interfaces.ICommandDispatcher
import interfaces.IServerInterface
import utils.Message
import utils.injection

class LocalServerInterface : IServerInterface {
    private val commandDispatcher by injection<ICommandDispatcher>()

    override suspend fun executeCommand(argv: List<String>): Message {
        val command = commandDispatcher.resolve(argv)
        command.run()
        return command.output
    }
}