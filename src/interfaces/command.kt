package interfaces

import utils.GetoptCallback
import utils.Parameters

open class CommandExecutionException(s: String, e: Throwable? = null) : Exception(s, e)
class CommandNotFoundException(name: String) : CommandExecutionException("command '$name' not found")
class CommandSyntaxException(message: String) : CommandExecutionException(message)

interface ICommand {
    val name: String
    val description: String
    val options: Map<String, GetoptCallback>

    var argv: List<String>
    var input: Parameters
    var output: Parameters

    fun parseArguments(argv: List<String>)
    suspend fun run()
    fun serverRun()
    suspend fun renderResult()
}
