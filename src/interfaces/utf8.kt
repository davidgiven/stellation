package interfaces

class EncodingFailureException(e: Exception): Exception(e)

interface IUtf8 {
    fun toString(array: ByteArray, offset: Int = 0, limit: Int = array.size): String
    fun toByteArray(string: String): ByteArray
}
