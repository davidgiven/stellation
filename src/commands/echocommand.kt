package commands

import utils.GetoptCallback
import utils.setSuccess

class EchoCommand : AbstractLocalCommand() {
    override val name = "echo"
    override val description = "prints stuff"

    override val options: Map<String, GetoptCallback> = emptyMap()

    override suspend fun run() {
        output.setSuccess(true)
    }

    override suspend fun renderResult() {
        val s = argv.toList().drop(1).joinToString(" ")
        console.println(s)
    }
}
