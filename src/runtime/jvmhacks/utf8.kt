package runtime.hacks

import java.nio.ByteBuffer
import java.nio.charset.CodingErrorAction
import java.nio.charset.MalformedInputException
import java.nio.charset.StandardCharsets

class EncodingFailureException(e: Exception): Exception(e)

private val utf8decoder = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)

fun String.toUtf8ByteArray() = this.toByteArray()

fun ByteArray.fromUtf8ByteArray(offset: Int = 0, limit: Int = this.size): String {
    try {
        val buffer = ByteBuffer.wrap(this, offset, limit)
        return utf8decoder.decode(buffer).toString()
    } catch (e: MalformedInputException) {
        throw EncodingFailureException(e)
    }
}
