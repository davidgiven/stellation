package commands

import interfaces.IClientInterface
import utils.injection

/* Commands which are remoted from the client to the server (if necessary). */
abstract class AbstractRemoteCommand : AbstractCommand() {
    val serverInterface by injection<IClientInterface>()

    override suspend fun run() {
        output = serverInterface.executeCommand(argv)
    }

    abstract override fun serverRun()
}
