package utils

import org.junit.Rule
import org.junit.rules.ExpectedException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class GetoptTest {
    @Rule @JvmField
    val thrown = ExpectedException.none()

    private var results: List<Pair<String, String>> = emptyList()

    private val callbacks: Map<String, (String) -> Int> = mapOf(
            "-v" to { _ -> results += Pair("-v", ""); 0 },
            "-f" to { f -> results += Pair("-f", f); 1 },
            "--verbose" to { _ -> results += Pair("--verbose", ""); 0 },
            "--file" to { f -> results += Pair("--file", f); 1 },
            FILE_OPTION to { f -> results += Pair(FILE_OPTION, f); 0 }
    )

    @Test
    fun empty() {
        getopt(emptyArray(), callbacks)
        assertTrue(results.isEmpty())
    }

    @Test
    fun shortFlag() {
        getopt(arrayOf("-v"), callbacks)
        assertEquals(listOf(Pair("-v", "")), results)
    }

    @Test
    fun longFlag() {
        getopt(arrayOf("--verbose"), callbacks)
        assertEquals(listOf(Pair("--verbose", "")), results)
    }

    @Test
    fun shortWithInlineParameter() {
        getopt(arrayOf("-fFNORD", "-v"), callbacks)
        assertEquals(listOf(Pair("-f", "FNORD"), Pair("-v", "")), results)
    }

    @Test
    fun shortWithOutOfLineParameter() {
        getopt(arrayOf("-f", "FNORD", "-v"), callbacks)
        assertEquals(listOf(Pair("-f", "FNORD"), Pair("-v", "")), results)
    }

    @Test
    fun longWithInlineParameter() {
        getopt(arrayOf("--file=FNORD", "--verbose"), callbacks)
        assertEquals(listOf(Pair("--file", "FNORD"), Pair("--verbose", "")), results)
    }

    @Test
    fun longWithOutOfLineParameter() {
        getopt(arrayOf("--file", "FNORD", "--verbose"), callbacks)
        assertEquals(listOf(Pair("--file", "FNORD"), Pair("--verbose", "")), results)
    }

    @Test
    fun withFile() {
        getopt(arrayOf("FNORD", "--verbose"), callbacks)
        assertEquals(listOf(Pair(FILE_OPTION, "FNORD"), Pair("--verbose", "")), results)
    }

    @Test
    fun missingShortParameter() {
        try {
            getopt(arrayOf("-f"), callbacks)
            fail("exception not thrown")
        } catch (_: MissingOptionException) {
        }
    }

    @Test
    fun missingLongParameter() {
        try {
            getopt(arrayOf("--file"), callbacks)
            fail("exception not thrown")
        } catch (_: MissingOptionException) {
        }
    }

    @Test
    fun invalidShortParameter() {
        try {
            getopt(arrayOf("-x"), callbacks)
            fail("exception not thrown")
        } catch (_: UnrecognisedOptionException) {
        }
    }

    @Test
    fun invalidLongParameter() {
        try {
            getopt(arrayOf("--xxx"), callbacks)
            fail("exception not thrown")
        } catch (_: UnrecognisedOptionException) {
        }
    }
}
