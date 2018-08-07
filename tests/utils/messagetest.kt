package utils

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class MessageTest {
    private val encodeDecodeTests: Map<Map<String, String>, String> = mapOf(
            /* Remember that encoded strings are sorted by key! */
            emptyMap<String, String>() to "",
            mapOf("foo" to "bar") to "foo=bar\n",
            mapOf("föǫ" to "bà®") to "föǫ=bà®\n",
            mapOf("foo" to "bar", "baz" to "boo") to "baz=boo\nfoo=bar\n",
            mapOf("foo=" to "=bar") to "foo%e=%ebar\n",
            mapOf("fo\no" to "b\nar") to "fo%no=b%nar\n",
            mapOf("%" to "%%") to "%p=%p%p\n"
    )

    @Test
    fun empty() {
        val p = Message()
        assertEquals(emptyMap(), p.toMap())
        assertEquals(emptyList(), p.toList())
    }

    @Test
    fun strings() {
        val p = Message()
        p["foo"] = "bar"
        p["baz"] = "boo"
        assertEquals(mapOf("foo" to "bar", "baz" to "boo"), p.toMap())
        assertEquals(emptyList(), p.toList())
        assertEquals("bar", p["foo"])
        assertEquals("boo", p["baz"])
    }

    @Test
    fun arguments() {
        val p = Message()
        p.addString("foo")
        p.addString("bar")
        assertEquals(mapOf("0" to "foo", "1" to "bar", "_count" to "2"), p.toMap())
        assertEquals(listOf("foo", "bar"), p.toList())
        assertEquals("foo", p[0])
        assertEquals("bar", p[1])
        assertEquals(2, p.count)
    }

    @Test
    fun mixed() {
        val p = Message()
        p["foo"] = "bar"
        p.addString("thingy")
        p["baz"] = "boo"
        assertEquals(mapOf("foo" to "bar", "baz" to "boo", "0" to "thingy", "_count" to "1"), p.toMap())
        assertEquals(listOf("thingy"), p.toList())
        assertEquals("bar", p["foo"])
        assertEquals("boo", p["baz"])
        assertEquals("thingy", p[0])
        assertEquals(1, p.count)
    }

    @Test fun types() {
        val p = Message()
        p.setChar("char", 'q')
        p.setShort("short", 1.toShort())
        p.setInt("int", 2)
        p.setFloat("float", (3.0).toFloat())
        p.setDouble("double", 4.0)
        p.setString("string", "five")
        assertEquals(
                mapOf(
                        "char" to "q",
                        "short" to "1",
                        "int" to "2",
                        "float" to "3.0",
                        "double" to "4.0",
                        "string" to "five"), p.toMap())
        assertEquals('q', p.getChar("char"))
        assertEquals(1.toShort(), p.getShort("short"))
        assertEquals(2, p.getInt("int"))
        assertEquals((3.0).toFloat(), p.getFloat("float"))
        assertEquals(4.0, p.getDouble("double"))
        assertEquals("five", p.getString("string"))
    }

    @Test fun missingByName() {
        val p = Message()
        assertEquals(null as Char?, p.getCharOrNull("char"))
        assertEquals(null as Short?, p.getShortOrNull("short"))
        assertEquals(null as Int?, p.getIntOrNull("int"))
        assertEquals(null as Float?, p.getFloatOrNull("float"))
        assertEquals(null as Double?, p.getDoubleOrNull("double"))
        assertEquals(null as String?, p.getStringOrNull("string"))
    }

    @Test fun defaults() {
        val p = Message()
        assertEquals('0', p.getCharOrDefault("char", '0'))
        assertEquals(0.toShort(), p.getShortOrDefault("short", 0.toShort()))
        assertEquals(0, p.getIntOrDefault("int", 0))
        assertEquals((0.0).toFloat(), p.getFloatOrDefault("float", (0.0).toFloat()))
        assertEquals(0.0, p.getDoubleOrDefault("double", 0.0))
        assertEquals("", p.getStringOrDefault("string", ""))
    }

    @Test fun contains() {
        var p = Message()
        p["foo"] = "bar"
        assertTrue("foo" in p)
        assertFalse("baz" in p)
    }

    @Test fun exceptionOnMissing() {
        val p = Message()
        try {
            p["fnord"]
            fail("exception not thrown")
        } catch (_: NullPointerException) {
        }
    }


    @Test
    fun encode() {
        encodeDecodeTests.forEach { e ->
            assertEquals(e.value, Message(e.key).serialise(), "encoding to '${e.value}'")
        }
    }

    @Test
    fun decode() {
        encodeDecodeTests.forEach { e ->
            assertEquals(e.key, Message(e.value).toMap(), "decoding '${e.value}'")
        }
    }

    @Test
    fun decodeFails() {
        val failingTestCases = listOf(
                "foo=bar=baz\n",
                "foo=bar%x\n"
        )

        failingTestCases.forEach { s ->
            try {
                Message(s)
                fail("uncaught exception decoding '$s'")
            } catch (f: Fault) {
                assertEquals(FaultDomain.INTERNAL, f.domain)
            }
        }
    }
}
