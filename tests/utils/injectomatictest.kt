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

        assertEquals(1, inject())
        assertEquals(1.0, inject())
        assertEquals("fnord", inject())
    }

    @Test
    fun complexTest() {
        var list = Subclass()
        bind<Superclass>(list)

        assertSame(list, inject<Superclass>())
    }

    @Test
    fun failingUpcasting() {
        bind(Superclass())

        try {
            inject<Subclass>()
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
