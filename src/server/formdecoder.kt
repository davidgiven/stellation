package server

import runtime.hacks.EncodingFailureException
import runtime.hacks.fromUtf8ByteArray

class BadFormEncodingException : BadCgiException("invalid form encoding")

fun decodeFormEncoding(source: ByteArray): Map<String, String> {
    var map: Map<String, String> = emptyMap()
    var tempArray = ByteArray(source.size)
    var ptrIn = 0
    var ptrOut = 0

    fun getChar(i: Int): Char {
        return if ((i < 0) || (i >= source.size)) '\u0000' else source.get(i).toChar()
    }

    fun accumulate() {
        ptrOut = 0
        while (true) {
            val c = getChar(ptrIn)
            when (c) {
                '\u0000', '=', '&' -> return
                '%'                -> {
                    val h = (fromHex(source[ptrIn + 1]) shl 4) or fromHex(source[ptrIn + 2])
                    tempArray[ptrOut++] = h.toByte()
                    ptrIn += 3
                }
                else               -> {
                    tempArray[ptrOut++] = c.toByte()
                    ptrIn++
                }
            }
        }
    }

    try {
        while (ptrIn < source.size) {
            accumulate()
            val key = tempArray.fromUtf8ByteArray(0, ptrOut)
            when (getChar(ptrIn++)) {
                '&'           -> {
                    if (!key.isEmpty()) {
                        map += key to ""
                    }
                }
                '\u0000', '=' -> {
                    if (key.isEmpty()) {
                        throw BadFormEncodingException()
                    }

                    accumulate()
                    val value = tempArray.fromUtf8ByteArray(0, ptrOut)
                    map += key to value

                    when (getChar(ptrIn++)) {
                        '\u0000', '&' -> {
                        }
                        else          -> throw BadFormEncodingException()
                    }
                }
                else          -> throw BadFormEncodingException()
            }
        }
    } catch (_: EncodingFailureException) {
        throw BadFormEncodingException()
    }

    return map
}

private fun fromHex(c: Char): Int = when (c) {
    in '0'..'9' -> (c - '0')
    in 'a'..'f' -> (c - 'a' + 10)
    in 'A'..'F' -> (c - 'A' + 10)
    else        -> throw BadFormEncodingException()
}

private fun fromHex(b: Byte) = fromHex(b.toChar())
