package commands

import interfaces.throwCommandSyntaxException
import model.SThing
import model.allProperties
import utils.Flags
import utils.Oid

class ExCommand : AbstractCommand() {
    override val name = "ex"
    override val description = "examines an object by oid"

    var oid: Oid = 0
    var allOption = false

    override val flags = Flags()
            .addFlag("--all") { allOption = true }
            .addFlag("-a") { allOption = true }

    override fun parseRemainingArguments(argv: List<String>) {
        if (argv.size != 1) {
            throwCommandSyntaxException("you must supply exactly one argument")
        }

        oid = argv[0].toInt()
    }

    override suspend fun renderResult() {
        val thing = model.loadObject(oid, SThing::class)

        val sb = StringBuilder()
        sb.append("${thing.oid}: ${thing.kind} ")
        if (thing.name.isNotBlank()) {
            sb.append("\"${thing.name}\" ")
        }
        if (thing.owner != null) {
            sb.append("belonging to ${thing.owner!!.name}")
        }
        console.println(sb.toString())

        var truncated = false
        for ((name, property) in allProperties) {
            if (property.hasValue(model, oid)) {
                var s = property.serialiseToString(model, oid)
                if (!allOption && (s.length > 50)) {
                    s = s.substring(0, 49) + "..."
                    truncated = true
                }

                console.println("  $name: $s")
            }
        }
        if (truncated) {
            console.println("long properties truncated; use -a to see everything")
        }
    }
}
