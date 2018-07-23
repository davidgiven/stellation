package runtime.jvm

import interfaces.EncodingFailureException
import interfaces.IUtf8
import java.nio.ByteBuffer
import java.nio.charset.CodingErrorAction
import java.nio.charset.MalformedInputException
import java.nio.charset.StandardCharsets

class JvmUtf8 : IUtf8 {
    private val utf8decoder = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)

    override fun toString(array: ByteArray, offset: Int, limit: Int): String {
        try {
            val buffer = ByteBuffer.wrap(array, offset, limit)
            return utf8decoder.decode(buffer).toString()
        } catch (e: MalformedInputException) {
            throw EncodingFailureException(e)
        }
    }

    override fun toByteArray(string: String): ByteArray = string.toByteArray()
}
