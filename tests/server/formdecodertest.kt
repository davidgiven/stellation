package server

import runtime.hacks.toUtf8ByteArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class FormDecoderTest {
    @Test
    fun empty() {
        assertEquals(emptyMap(), decode(""))
    }

    @Test
    fun single() {
        assertEquals(mapOf("foo" to "bar"), decode("foo=bar"))
    }

    @Test
    fun double() {
        assertEquals(mapOf("foo" to "bar", "baz" to "boo"), decode("foo=bar&baz=boo"))
    }

    @Test
    fun utf8() {
        assertEquals(mapOf("encoded" to "lörickè"), decode("encoded=l%C3%B6rick%C3%A8"))
    }

    @Test
    fun doubleWithMissingLastPair() {
        assertEquals(mapOf("foo" to "bar"), decode("foo=bar&"))
    }

    @Test
    fun doubleWithMissingFirstPair() {
        assertEquals(mapOf("foo" to "bar"), decode("&foo=bar"))
    }

    @Test
    fun tripleWithMissingMiddlePair() {
        assertEquals(mapOf("foo" to "bar", "baz" to "boo"), decode("foo=bar&&baz=boo"))
    }

    @Test
    fun badHex() {
        try {
            decode("foo=%ZZ")
            fail("exception not thrown")
        } catch (_: BadFormEncodingException) {
        }
    }

    @Test
    fun badUtf8() {
        try {
            decode("foo=%c3%28")
            fail("exception not thrown")
        } catch (_: BadFormEncodingException) {
        }
    }
}

private fun decode(s: String) = decodeFormEncoding(s.toUtf8ByteArray())
