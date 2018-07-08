package utils

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.fail

class InjectomaticTest {
    open class Superclass
    class Subclass : Superclass()

    @BeforeTest
    fun setup() {
        resetBindingsForTest()
    }

    @Test
    fun simpleTest() {
        bind(1)
        bind(1.0)
        bind("fnord")

        assertEquals(1, get())
        assertEquals(1.0, get())
        assertEquals("fnord", get())
    }

    @Test
    fun complexTest() {
        var list = Subclass()
        bind<Superclass>(list)

        assertSame(list, get<Superclass>())
    }

    @Test
    fun failingUpcasting() {
        bind(Superclass())

        try {
            get<Subclass>()
            fail()
        } catch (_: InjectomaticException) {
        }
    }

    @Test
    fun multipleBindingsFail() {
        try {
            bind(1)
            bind(1)
        } catch (_: InjectomaticException) {
        }
    }
}
