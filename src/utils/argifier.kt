package utils

import utils.FaultDomain.SYNTAX

fun throwBadStringEscapeException(): Nothing = throw Fault(SYNTAX).withDetail("bad string escape")
fun throwUnterminatedStringException(): Nothing = throw Fault(SYNTAX).withDetail("unterminated string")

fun argify(input: String): List<String> {
    val builders = ArrayList<StringBuilder>()
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
            else                 -> throwBadStringEscapeException()
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

        throwUnterminatedStringException()
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

fun unargify(argv: List<String>): String {
    var words: List<String> = emptyList()
    for (arg in argv) {
        var word = arg
        word = word.replace("\\", "\\\\")
        if (' ' in word) {
            word = '"' + word.replace("'", "\\'").replace("\"", "\\\"") + '"'
        } else {
            word = word.replace("'", "\\'").replace("\"", "\\\"")
        }
        words += word
    }
    return words.joinToString(" ")
}
