package interfaces

import utils.Fault
import utils.FaultDomain.SYNTAX
import utils.Flags
import utils.Message

fun throwCommandNotFoundException(name: String): Nothing =
        throw Fault(SYNTAX).withDetail("command '$name' not found")

fun throwCommandSyntaxException(message: String): Nothing =
        throw Fault(SYNTAX).withDetail(message)

private const val SUCCESS = "_success"

class CommandMessage : Message() {
    fun setSuccess(success: Boolean) = setBoolean(SUCCESS, success)
    fun getSuccess(): Boolean = getBooleanOrDefault(SUCCESS, false)
}

interface ICommand {
    val name: String
    val description: String
    val flags: Flags

    var argv: List<String>
    var input: CommandMessage
    var output: CommandMessage

    fun parseArguments(argv: List<String>)
    fun parseRemainingArguments(argv: List<String>)
    fun validateArguments()
    suspend fun run()
    fun serverRun()
    suspend fun renderResult()
}
