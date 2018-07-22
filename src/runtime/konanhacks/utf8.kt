package runtime.hacks

import kotlin.text.toUtf8
import kotlin.text.stringFromUtf8OrThrow

class EncodingFailureException(e: Exception): Exception(e)

fun String.toUtf8ByteArray() = this.toUtf8()

fun ByteArray.fromUtf8ByteArray(offset: Int = 0, limit: Int = this.size): String {
    try {
        return this.stringFromUtf8OrThrow(offset, limit)
    } catch (e: IllegalCharacterConversionException) {
        throw EncodingFailureException(e)
    }
}

