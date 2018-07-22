package commands

import utils.Mailbox

abstract class AbstractClientCommand: AbstractCommand() {
    override fun serverRun(argv: List<String>) {}
}
