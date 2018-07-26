package runtime.shared

import interfaces.ICommandDispatcher
import interfaces.IServerInterface
import utils.Parameters
import utils.injection

class LocalServerInterface : IServerInterface {
    private val commandDispatcher by injection<ICommandDispatcher>()

    override suspend fun executeCommand(argv: List<String>): Parameters {
        val command = commandDispatcher.resolve(argv)
        command.run()
        return command.output
    }
}