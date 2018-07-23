package runtime.konan

import interfaces.EncodingFailureException
import interfaces.IUtf8
import kotlin.text.toUtf8
import kotlin.text.stringFromUtf8OrThrow

class KonanUtf8 : IUtf8 {
    override fun toString(array: ByteArray, offset: Int, limit: Int): String {
        try {
            return array.stringFromUtf8OrThrow(offset, limit)
        } catch (e: IllegalCharacterConversionException) {
            throw EncodingFailureException(e)
        }
    }

    override fun toByteArray(string: String): ByteArray = string.toUtf8()
}
