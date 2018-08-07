package runtime.shared

import utils.resetBindingsForTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class ServerMessageTest {
    @BeforeTest
    fun setup() {
        resetBindingsForTest()
    }

    @Test
    fun commandInputExists() {
        val argv = listOf("foo", "bar", "baz")
        val p = ServerMessage()
        p.setCommandInput(argv)

        assertTrue(p.hasCommandInput())
        assertEquals(argv, p.getCommandInput())
    }

    @Test
    fun getSetClock() {
        val p = ServerMessage()
        p.setClock(1.234)
        assertEquals(1.234, p.getClock())
    }
}
