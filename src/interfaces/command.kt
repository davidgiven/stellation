package interfaces

import utils.Flags
import utils.Message
import utils.getOrDefault
import utils.set

open class CommandExecutionException(s: String, e: Throwable? = null) : Exception(s, e)
class CommandNotFoundException(name: String) : CommandExecutionException("command '$name' not found")
class CommandSyntaxException(message: String) : CommandExecutionException(message)

private const val SUCCESS = "_success"

class CommandMessage : Message() {
    fun setSuccess(success: Boolean) = set(SUCCESS, success)
    fun getSuccess(): Boolean = getOrDefault(SUCCESS, false)
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