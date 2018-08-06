package commands

import interfaces.IAuthenticator
import interfaces.throwCommandSyntaxException
import model.SThing
import model.allProperties
import utils.Flags
import utils.Oid
import utils.injection

class RenameCommand : AbstractRemoteCommand() {
    override val name = "rename"
    override val description = "renames an object"

    var oid: Oid = 0
    var newName: String = ""

    private val authenticator by injection<IAuthenticator>()

    override fun parseRemainingArguments(argv: List<String>) {
        if (argv.size != 2) {
            throwCommandSyntaxException("you must supply exactly two arguments")
        }

        oid = argv[0].toInt()
        newName = argv[1]
    }

    override fun serverRun() {
        var o = model.loadObject(oid, SThing::class)
        o.checkModificationAccess()
        o.name = newName
    }
}
