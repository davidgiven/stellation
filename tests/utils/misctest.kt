package utils

import kotlin.test.Test
import kotlin.test.assertEquals

class MiscTest {
    @Test
    fun roundTest() {
        assertEquals(1.0, (1.0).roundBy(10.0))
        assertEquals(1.4, (1.4).roundBy(10.0))
        assertEquals(1.4, (1.44).roundBy(10.0))
        assertEquals(1.5, (1.46).roundBy(10.0))

        assertEquals(-1.0, (-1.0).roundBy(10.0))
        assertEquals(-1.4, -(1.4).roundBy(10.0))
        assertEquals(-1.4, -(1.44).roundBy(10.0))
        assertEquals(-1.5, -(1.46).roundBy(10.0))

        assertEquals(0.0, (0.0).roundBy(10.0))
    }
}

