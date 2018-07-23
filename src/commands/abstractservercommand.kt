package commands

import utils.Mailbox

abstract class AbstractServerCommand: AbstractCommand() {
    val mailbox = Mailbox<Parameters>()

    override suspend fun run() {
        val serverParams = Parameters()
        argv.forEachIndexed { i, arg -> serverParams.set(i).to(arg) }

        output = mailbox.wait()
    }

    abstract fun serverRun()
}
