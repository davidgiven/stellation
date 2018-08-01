package runtime.shared

import utils.Codec
import utils.bind
import utils.resetBindingsForTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class ServerMessageTest {
    @BeforeTest
    fun setup() {
        resetBindingsForTest()
        bind(Codec())
    }

    @Test
    fun commandInputExists() {
        val argv = listOf("foo", "bar", "baz")
        val p = ServerMessage()
        p.setCommandInput(argv)

        assertTrue(p.hasCommandInput())
        assertEquals(argv, p.getCommandInput())
    }
}
