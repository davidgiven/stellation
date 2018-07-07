package model

import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class FrameTest {
    private val WIDTH = 5
    private val HEIGHT = 4
    private val DATA = listOf(
            "00000",
            "01111",
            "01221",
            "01111").joinToString("")
    private val REP = "$WIDTH#$HEIGHT#$DATA"

    @BeforeTest
    fun before() {
        assertEquals(WIDTH*HEIGHT, DATA.length)
    }

    @Test
    fun createFrame() {
        val frame = Frame(REP)
        assertEquals(WIDTH, frame.width)
        assertEquals(HEIGHT, frame.height)
        assertEquals(REP, frame.serialise())
    }
}
