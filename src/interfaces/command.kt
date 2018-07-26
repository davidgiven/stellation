package interfaces

import utils.GetoptCallback
import utils.Message
import utils.getOrDefault
import utils.set

open class CommandExecutionException(s: String, e: Throwable? = null) : Exception(s, e)
class CommandNotFoundException(name: String) : CommandExecutionException("command '$name' not found")
class CommandSyntaxException(message: String) : CommandExecutionException(message)

class CommandMessage : Message() {
    fun setSuccess(success: Boolean) = set("_success", success)
    fun getSuccess(): Boolean = getOrDefault("_success", false)
}

interface ICommand {
    val name: String
    val description: String
    val options: Map<String, GetoptCallback>

    var argv: List<String>
    var input: CommandMessage
    var output: CommandMessage

    fun parseArguments(argv: List<String>)
    suspend fun run()
    fun serverRun()
    suspend fun renderResult()
}
