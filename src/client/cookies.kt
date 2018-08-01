package client

import kotlin.browser.document
import kotlin.js.Date

private const val PREFIX = "com.cowlark.stellation."

private external fun decodeURIComponent(string: String): String
private external fun encodeURIComponent(string: String): String

class Cookies {
    private val SPLITTER = Regex("; *")

    operator fun set(name: String, value: String) {
        val expiry = Date(Date().getTime() + (1000 * 3600 * 24 * 365)).toUTCString()
        val encodedName = encodeURIComponent(name)
        val encodedValue = encodeURIComponent(value)
        document.cookie = "$PREFIX$encodedName=$encodedValue;expires=$expiry;path=/"
    }

    operator fun get(name: String): String? {
        val encodedName = PREFIX + encodeURIComponent(name)
        val pairs = document.cookie.split(SPLITTER)
        for (pair in pairs) {
            if (pair.startsWith("$encodedName=")) {
                val encodedValue = pair.substringAfter('=')
                return decodeURIComponent(encodedValue)
            }
        }
        return null
    }
}