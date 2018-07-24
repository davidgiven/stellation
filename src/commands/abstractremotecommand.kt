package commands

import utils.Codec
import utils.Mailbox
import utils.Parameters
import utils.get
import utils.set

const val COMMAND_PARAMETER = "_command"

/* Commands which are remoted from the client to the server (if necessary). */
abstract class AbstractRemoteCommand: AbstractCommand() {
    val mailbox = Mailbox<Parameters>()

    override suspend fun run() {
        val serverParams = Parameters()
        serverParams[COMMAND_PARAMETER] = name
        argv.forEachIndexed { i, arg -> serverParams[i] = arg }

        val codec: Codec = get()
        val encoded = codec.encode(serverParams.toMap())
        console.println(encoded)

        output = mailbox.wait()
    }

    abstract fun serverRun()
}
