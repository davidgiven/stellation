package commands

class EchoCommand : AbstractLocalCommand() {
    override val name = "echo"
    override val description = "prints stuff"

    override fun parseRemainingArguments(argv: List<String>) {
    }

    override suspend fun renderResult() {
        val s = argv.toList().drop(1).joinToString(" ")
        console.println(s)
    }
}
