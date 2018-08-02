package commands

import interfaces.IAuthenticator
import interfaces.Oid
import interfaces.throwCommandSyntaxException
import model.checkGod
import model.currentPlayer
import utils.Flags
import utils.injection

class SetPasswordCommand : AbstractRemoteCommand() {
    override val name = "set-password"
    override val description = "changes a user's password"

    private val authenticator by injection<IAuthenticator>()

    var userOption: Oid = 0
    var passwordOption: String = ""

    override val flags = Flags()
            .addInt("--user", ::userOption)
            .addString("--password", ::passwordOption)

    override fun validateArguments() {
        if (passwordOption == "") {
            throwCommandSyntaxException("a new password must be specified")
        }
        if (userOption == 0) {
            userOption = authenticator.currentPlayerOid
        } else {
            model.currentPlayer().checkGod()
        }
    }

    override fun serverRun() {
        authenticator.setPassword(userOption, passwordOption)
        output.setSuccess(true)
    }
}
