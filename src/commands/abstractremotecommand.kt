package commands

import interfaces.IServerInterface
import utils.get
import utils.lazyget

/* Commands which are remoted from the client to the server (if necessary). */
abstract class AbstractRemoteCommand : AbstractCommand() {
    val serverInterface: IServerInterface by lazyget()

    override suspend fun run() {
        output = serverInterface.executeCommand(argv)
    }

    abstract override fun serverRun()
}
