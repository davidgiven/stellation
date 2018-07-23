package commands

import utils.Mailbox

abstract class AbstractServerCommand: AbstractCommand() {
    val mailbox = Mailbox<Parameters>()

    override suspend fun run() {
        val serverParams = Parameters()
        serverParams.setCommand(name)
        argv.forEachIndexed { i, arg -> serverParams.param(i).to(arg) }

        output = mailbox.wait()
    }

    abstract fun serverRun()
}
