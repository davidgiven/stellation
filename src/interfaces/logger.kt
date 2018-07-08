package interfaces

interface ILogger {
    fun println(message: String)
}

fun ILogger.log(vararg messages: String) {
    var sb = StringBuilder()
    for (s in messages) {
        sb.append(s)
    }
    this.println("Log: $sb")
}
