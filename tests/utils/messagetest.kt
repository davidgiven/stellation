package utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class MessageTest {
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
        p.add("foo")
        p.add("bar")
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
        p.add("thingy")
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
        p["char"] = 'q'
        p["short"] = 1.toShort()
        p["int"] = 2
        p["float"] = (3.0).toFloat()
        p["double"] = 4.0
        p["string"] = "five"
        assertEquals(
                mapOf(
                        "char" to "q",
                        "short" to "1",
                        "int" to "2",
                        "float" to "3.0",
                        "double" to "4.0",
                        "string" to "five"), p.toMap())
        assertEquals('q', p["char"])
        assertEquals(1.toShort(), p["short"])
        assertEquals(2, p["int"])
        assertEquals((3.0).toFloat(), p["float"])
        assertEquals(4.0, p["double"])
        assertEquals("five", p["string"])
    }

    @Test fun missingByName() {
        val p = Message()
        assertEquals(null as Char?, p.getOrNull("char"))
        assertEquals(null as Short?, p.getOrNull("short"))
        assertEquals(null as Int?, p.getOrNull("int"))
        assertEquals(null as Float?, p.getOrNull("float"))
        assertEquals(null as Double?, p.getOrNull("double"))
        assertEquals(null as String?, p.getOrNull("string"))
    }

    @Test fun defaults() {
        val p = Message()
        assertEquals('0', p.getOrDefault("char", '0'))
        assertEquals(0.toShort(), p.getOrDefault("short", 0.toShort()))
        assertEquals(0, p.getOrDefault("int", 0))
        assertEquals((0.0).toFloat(), p.getOrDefault("float", (0.0).toFloat()))
        assertEquals(0.0, p.getOrDefault("double", 0.0))
        assertEquals("", p.getOrDefault("string", ""))
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
            p.get<String, Int>("fnord")
            fail("exception not thrown")
        } catch (_: NullPointerException) {
        }
    }
}
