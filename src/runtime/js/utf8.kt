package runtime.js

import interfaces.UNIMPLEMENTED
import interfaces.IUtf8

private external fun decodeURIComponent(string: String): String
private external fun encodeURIComponent(string: String): String
private external fun escape(string: String): String
private external fun unescape(string: String): String

class JsUtf8 : IUtf8 {
    override fun toString(array: ByteArray, offset: Int, limit: Int): String = UNIMPLEMENTED()
    override fun toByteArray(string: String): ByteArray = UNIMPLEMENTED()
}
