package commands

import utils.Mailbox

abstract class AbstractServerCommand: AbstractCommand() {
    val mailbox = Mailbox<Unit>()

    override suspend fun clientRun(argv: List<String>) {
        mailbox.wait()
    }
}
