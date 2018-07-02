package model

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class FrameTest {
    private val WIDTH = 5
    private val HEIGHT = 4
    private val DATA = listOf(
            "00000",
            "01111",
            "01221",
            "01111").joinToString("")
    private val REP = "$WIDTH#$HEIGHT#$DATA"

    @Before
    fun before() {
        assertThat(DATA).hasLength(WIDTH * HEIGHT)
    }

    @Test
    fun createFrame() {
        val frame = Frame(REP)
        assertThat(frame.width).isEqualTo(WIDTH)
        assertThat(frame.height).isEqualTo(HEIGHT)
        assertThat(frame.serialise()).isEqualTo(REP)
    }
}
