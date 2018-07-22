package commands

import model.Model
import utils.get

abstract class AbstractCommand {
    protected val model: Model = get()

    open val needsAuthentication = true
    abstract val name: String
    abstract val description: String

    abstract suspend fun clientRun(argv: List<String>)
    abstract fun serverRun(argv: List<String>)
}
