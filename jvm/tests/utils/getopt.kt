package utils

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.rules.ExpectedException
import kotlin.test.Test

class GetoptTest {
    @Rule @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    private var results: Array<Pair<String, String>> = emptyArray()

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
        assertThat(results.asList()).isEmpty()
    }

    @Test
    fun shortFlag() {
        getopt(arrayOf("-v"), callbacks)
        assertThat(results.asList()).containsExactly(Pair("-v", ""))
    }

    @Test
    fun longFlag() {
        getopt(arrayOf("--verbose"), callbacks)
        assertThat(results.asList()).containsExactly(Pair("--verbose", ""))
    }

    @Test
    fun shortWithInlineParameter() {
        getopt(arrayOf("-fFNORD", "-v"), callbacks)
        assertThat(results.asList()).containsExactly(Pair("-f", "FNORD"), Pair("-v", ""))
    }

    @Test
    fun shortWithOutOfLineParameter() {
        getopt(arrayOf("-f", "FNORD", "-v"), callbacks)
        assertThat(results.asList()).containsExactly(Pair("-f", "FNORD"), Pair("-v", ""))
    }

    @Test
    fun longWithInlineParameter() {
        getopt(arrayOf("--file=FNORD", "--verbose"), callbacks)
        assertThat(results.asList()).containsExactly(Pair("--file", "FNORD"), Pair("--verbose", ""))
    }

    @Test
    fun longWithOutOfLineParameter() {
        getopt(arrayOf("--file", "FNORD", "--verbose"), callbacks)
        assertThat(results.asList()).containsExactly(Pair("--file", "FNORD"), Pair("--verbose", ""))
    }

    @Test
    fun withFile() {
        getopt(arrayOf("FNORD", "--verbose"), callbacks)
        assertThat(results.asList()).containsExactly(Pair(FILE_OPTION, "FNORD"), Pair("--verbose", ""))
    }

    @Test
    fun missingShortParameter() {
        thrown.expect(MissingOptionException::class.java)
        getopt(arrayOf("-f"), callbacks)
    }

    @Test
    fun missingLongParameter() {
        thrown.expect(MissingOptionException::class.java)
        getopt(arrayOf("--file"), callbacks)
    }

    @Test
    fun invalidShortParameter() {
        thrown.expect(UnrecognisedOptionException::class.java)
        getopt(arrayOf("-x"), callbacks)
    }

    @Test
    fun invalidLongParameter() {
        thrown.expect(UnrecognisedOptionException::class.java)
        getopt(arrayOf("--xxx"), callbacks)
    }
}
