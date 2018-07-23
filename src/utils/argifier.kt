package utils

open class CommandLineParseException(s: String) : Exception("failed to parse command line: $s")
class BadStringEscapeException : CommandLineParseException("bad string escape")
class UnterminatedStringException : CommandLineParseException("unterminated string")

fun argify(input: String): List<String> {
    var builders: List<StringBuilder> = emptyList()
    var current: StringBuilder? = null
    var index = 0

    fun finished() = (index >= input.length)

    fun consumeWhitespace() {
        while (!finished()) {
            val c = input[index]
            if (!c.isWhitespace()) {
                return
            }
            index++
        }
    }

    fun unescape(): Char {
        val e = input.getOrElse(index++) { '\u0000' }
        return when (e) {
            '\\', '"', '\'', ' ' -> e
            else                 -> throw BadStringEscapeException()
        }
    }

    fun appendText() {
        while (!finished()) {
            var c = input[index]
            if (c.isWhitespace() || (c == '"') || (c == '\'')) {
                return
            }
            index++
            if (c == '\\') {
                c = unescape()
            }

            current!!.append(c)
        }
    }

    fun appendString() {
        val terminator = input[index++]
        while (!finished()) {
            var c = input[index++]
            if (c == terminator) {
                return
            }

            if ((c == '\\') && (terminator == '"')) {
                c = unescape()
            }

            current!!.append(c)
        }

        throw UnterminatedStringException()
    }

    while (!finished()) {
        consumeWhitespace()
        if (finished()) {
            break
        }

        current = StringBuilder()
        builders += current

        wordloop@ while (!finished()) {
            val c = input[index]
            when {
                c.isWhitespace()          -> break@wordloop
                (c == '"') or (c == '\'') -> appendString()
                else                      -> appendText()
            }
        }
    }

    return builders.map(StringBuilder::toString)
}