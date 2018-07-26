package commands

import interfaces.IServerInterface
import utils.injection

/* Commands which are remoted from the client to the server (if necessary). */
abstract class AbstractRemoteCommand : AbstractCommand() {
    val serverInterface by injection<IServerInterface>()

    override suspend fun run() {
        output = serverInterface.executeCommand(argv)
    }

    abstract override fun serverRun()
}
