package utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ArgifierTest {
    private val successfulCases: Map<String, List<String>> = mapOf(

            "'f'o'o'" to listOf("foo"),
            "" to emptyList(),
            "foo" to listOf("foo"),
            "foo bar" to listOf("foo", "bar"),
            "foo bar baz" to listOf("foo", "bar", "baz"),
            "  foo   bar  baz  " to listOf("foo", "bar", "baz"),

            "'foo'" to listOf("foo"),
            "'f'o'o'" to listOf("foo"),
            "\"foo\"" to listOf("foo"),
            "\"f\"o\"o\"" to listOf("foo"),

            "'foo bar' baz" to listOf("foo bar", "baz"),
            "\"foo bar\" baz" to listOf("foo bar", "baz"),

            "foo\\ bar" to listOf("foo bar"),
            "foo\\\"bar" to listOf("foo\"bar"),
            "\"foo\\\"bar\"" to listOf("foo\"bar")
    )

    private val unterminatedStringCases: List<String> = listOf(
            "foo 'foo",
            "foo \"foo",
            "'foo\\'bar'",
            "'",
            "\""
    )

    private val illegalEscapeCases: List<String> = listOf(
            "foo\\zbar",
            "foobar\\"
    )

    @Test
    fun successfulParsing() {
        successfulCases.forEach { e ->
            try {
                assertEquals(e.value, argify(e.key))
            } catch (t: Throwable) {
                throw AssertionError("while parsing ${e.key}", t)
            }
        }
    }

    @Test
    fun unterminatedStrings() {
        unterminatedStringCases.forEach { e ->
            try {
                argify(e)
                fail("exception not thrown while parsing $e")
            } catch (_: UnterminatedStringException) {
            }
        }
    }

    @Test
    fun illegalEscapes() {
        illegalEscapeCases.forEach { e ->
            try {
                argify(e)
                fail("exception not thrown while parsing $e")
            } catch (_: BadStringEscapeException) {
            }
        }
    }

    @Test
    fun unargify() {
        assertEquals("foo bar", utils.unargify(listOf("foo", "bar")))
        assertEquals("\"foo bar\"", utils.unargify(listOf("foo bar")))
        assertEquals("\\\"foo", utils.unargify(listOf("\"foo")))
        assertEquals("\\'foo", utils.unargify(listOf("'foo")))
    }
}

