package runtime.shared

import interfaces.ICommandDispatcher
import interfaces.IServerInterface
import utils.Parameters
import utils.get

class LocalServerInterface(val commandDispatcher: ICommandDispatcher = get()) : IServerInterface {
    override suspend fun executeCommand(argv: List<String>): Parameters {
        val command = commandDispatcher.resolve(argv)
        command.run()
        return command.output
    }
}