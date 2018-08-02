package utils

import org.junit.Rule
import org.junit.rules.ExpectedException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class GetoptTest {
    @Rule @JvmField
    val thrown = ExpectedException.none()

    private var vFlag = false
    private var fFlag = ""
    private var verboseFlag = false
    private var fileFlag = ""
    private var intFlag = 0
    private var boolFlag = false

    private val options = Flags()
            .addFlag("-v") { vFlag = true }
            .addString("-f", ::fFlag)
            .addFlag("--verbose") { verboseFlag = true }
            .addString("--file", ::fileFlag)
            .addInt("--int", ::intFlag)
            .addBoolean("-b", ::boolFlag)

    @Test
    fun empty() {
        val remaining = getopt(emptyArray(), options)
        assertFalse(vFlag)
        assertTrue(remaining.isEmpty())
    }

    @Test
    fun shortFlag() {
        val remaining = getopt(arrayOf("-v"), options)
        assertTrue(vFlag)
        assertTrue(remaining.isEmpty())
    }

    @Test
    fun longFlag() {
        val remaining = getopt(arrayOf("--verbose"), options)
        assertTrue(verboseFlag)
        assertTrue(remaining.isEmpty())
    }

    @Test
    fun shortWithInlineParameter() {
        val remaining = getopt(arrayOf("-fFNORD", "-v"), options)
        assertEquals("FNORD", fFlag)
        assertTrue(vFlag)
        assertTrue(remaining.isEmpty())
    }

    @Test
    fun shortWithOutOfLineParameter() {
        val remaining = getopt(arrayOf("-f", "FNORD", "-v"), options)
        assertEquals("FNORD", fFlag)
        assertTrue(vFlag)
        assertTrue(remaining.isEmpty())
    }

    @Test
    fun longWithInlineParameter() {
        val remaining = getopt(arrayOf("--file=FNORD", "--verbose"), options)
        assertEquals("FNORD", fileFlag)
        assertTrue(verboseFlag)
        assertTrue(remaining.isEmpty())
    }

    @Test
    fun longWithOutOfLineParameter() {
        val remaining = getopt(arrayOf("--file", "FNORD", "--verbose"), options)
        assertEquals("FNORD", fileFlag)
        assertTrue(verboseFlag)
        assertTrue(remaining.isEmpty())
    }

    @Test
    fun withNonParameterFirst() {
        val remaining = getopt(arrayOf("FNORD", "--verbose"), options)
        assertFalse(verboseFlag)
        assertEquals(listOf("FNORD", "--verbose"), remaining)
    }

    @Test
    fun withNonParameterLast() {
        val remaining = getopt(arrayOf("--verbose", "FNORD"), options)
        assertTrue(verboseFlag)
        assertEquals(listOf("FNORD"), remaining)
    }

    @Test
    fun missingShortParameter() {
        try {
            getopt(arrayOf("-f"), options)
            fail("exception not thrown")
        } catch (f: Fault) {
            assertEquals(FaultDomain.SYNTAX, f.domain)
            assertContains("missing", f.detail)
        }
    }

    @Test
    fun missingLongParameter() {
        try {
            getopt(arrayOf("--file"), options)
            fail("exception not thrown")
        } catch (f: Fault) {
            assertEquals(FaultDomain.SYNTAX, f.domain)
            assertContains("missing", f.detail)
        }
    }

    @Test
    fun invalidShortParameter() {
        try {
            getopt(arrayOf("-x"), options)
            fail("exception not thrown")
        } catch (f: Fault) {
            assertEquals(FaultDomain.SYNTAX, f.domain)
            assertContains("unrecognised", f.detail)
        }
    }

    @Test
    fun invalidLongParameter() {
        try {
            getopt(arrayOf("--xxx"), options)
            fail("exception not thrown")
        } catch (f: Fault) {
            assertEquals(FaultDomain.SYNTAX, f.domain)
            assertContains("unrecognised", f.detail)
        }
    }

    private fun assertContains(needle: String, haystack: String) {
        assertTrue(needle in haystack)
    }
}
