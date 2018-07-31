package commands

import interfaces.CommandSyntaxException
import model.checkGod
import model.currentPlayer
import utils.Flags

class SetPasswordCommand : AbstractRemoteCommand() {
    override val name = "set-password"
    override val description = "changes a user's password"

    var userOption: String = ""
    var passwordOption: String = ""

    override val flags = Flags()
            .addString("--user", ::userOption)
            .addString("--password", ::passwordOption)

    override fun validateArguments() {
        if (passwordOption == "") {
            throw CommandSyntaxException("a new password must be specified")
        }
        if (userOption != "") {
            model.currentPlayer().checkGod()
        }
    }

    override fun serverRun() {
        output.setSuccess(true)
    }
}
