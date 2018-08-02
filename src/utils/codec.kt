package utils

import utils.FaultDomain.INTERNAL

/* Translates a map of strings into an opaque, encoded String (and back again).
 *
 * The encoded syntax is nigh-trivial: it's a newline-separated KEY=VALUE list. KEY
 * and VALUE are escaped as follows.
 */

private const val ESCAPED_PERCENT = "%p"
private const val ESCAPED_NEWLINE = "%n"
private const val ESCAPED_EQUALS = "%e"

fun throwInvalidCodecDataException(s: String, e: Throwable? = null): Nothing =
        throw Fault(e).withDomain(INTERNAL).withDetail("invalid encoded packet: $s")

class Codec {
    private val ENCODE_REGEX = Regex("[%=\n]")

    private val ENCODE_TRANSFORMED = { r: MatchResult ->
        when (r.value) {
            "%"  -> ESCAPED_PERCENT
            "\n" -> ESCAPED_NEWLINE
            "="  -> ESCAPED_EQUALS
            else -> r.value
        }
    }

    private val DECODE_REGEX = Regex("%.")

    private val DECODE_TRANSFORMED = { r: MatchResult ->
        when (r.value) {
            ESCAPED_PERCENT -> "%"
            ESCAPED_NEWLINE -> "\n"
            ESCAPED_EQUALS  -> "="
            else            -> throwInvalidCodecDataException("bad string escape ${r.value}")
        }
    }

    fun encode(input: Map<String, String>): String {
        val sb = StringBuilder()

        input.forEach {
            sb.append(encodeString(it.key))
            sb.append('=')
            sb.append(encodeString(it.value))
            sb.append('\n')
        }

        return sb.toString()
    }

    fun decode(input: String): Map<String, String> {
        var map: Map<String, String> = emptyMap()

        input.lineSequence().forEach { line ->
            if (!line.isEmpty()) {
                val tokens = line.split("=")
                if (tokens.size != 2) {
                    throwInvalidCodecDataException("couldn't parse line")
                }
                map += decodeString(tokens[0]) to decodeString(tokens[1])
            }
        }

        return map
    }

    fun encodeString(string: String): String = ENCODE_REGEX.replace(string, ENCODE_TRANSFORMED)
    fun decodeString(string: String): String = DECODE_REGEX.replace(string, DECODE_TRANSFORMED)
}
