package interfaces

interface Logger {
    fun println(message: String)
}

fun Logger.log(vararg messages: String) {
    var sb = StringBuilder()
    for (s in messages) {
        sb.append(s)
    }
    this.println("Log: " + sb)
}
