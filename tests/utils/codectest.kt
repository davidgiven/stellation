package utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class CodecTest {
    private val codec = Codec()

    private val workingTestCases: Map<Map<String, String>, String> = mapOf(
            emptyMap<String, String>() to "",
            mapOf("foo" to "bar") to "foo=bar\n",
            mapOf("föǫ" to "bà®") to "föǫ=bà®\n",
            mapOf("foo" to "bar", "baz" to "boo") to "foo=bar\nbaz=boo\n",
            mapOf("foo=" to "=bar") to "foo%e=%ebar\n",
            mapOf("fo\no" to "b\nar") to "fo%no=b%nar\n",
            mapOf("%" to "%%") to "%p=%p%p\n"
    )

    private val failingTestCases: List<String> = listOf(
            "foo=bar=baz\n",
            "foo=bar%x\n"
    )

    @Test
    fun encode() {
        workingTestCases.forEach { e ->
            assertEquals(e.value, codec.encode(e.key), "encoding to '${e.value}'")
        }
    }

    @Test
    fun decode() {
        workingTestCases.forEach { e ->
            assertEquals(e.key, codec.decode(e.value), "decoding '${e.value}'")
        }
    }

    @Test
    fun decodeFails() {
        failingTestCases.forEach { s ->
            try {
                codec.decode(s)
                fail("uncaught exception decoding '$s'")
            } catch (_: InvalidCodecDataException) {
            }
        }
    }
}
