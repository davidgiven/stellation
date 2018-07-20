package ui

import interfaces.IUi
import interfaces.IUiElement
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.yield
import runtime.jvm.JvmStubUi
import kotlin.test.Test
import kotlin.test.assertEquals

class TestForm(ui: IUi) : AbstractForm<String>(ui) {
    lateinit var okButton: IUiElement
    lateinit var cancelButton: IUiElement

    override fun createTitlebar(div: IUiElement) {
        div.addText("p", "Titlebar")
    }

    override fun createUserInterface(div: IUiElement) {
        div.addText("p", "Text")
    }

    override fun createButtonBox(div: IUiElement) {
        var (okButton, cancelButton) = createYesNoButtonBox(div)
        this.okButton = okButton
        this.cancelButton = cancelButton

        okButton.onActivate {
            post("ok")
        }

        cancelButton.onActivate {
            post("cancel")
        }
    }
}

class AbstractFormTest {
    val ui = JvmStubUi()
    val form = TestForm(ui).show()

    @Test
    fun simplePost() {
        runBlocking {
            form.post("fnord")
            assertEquals("fnord", form.execute())
        }
    }

    @Test
    fun okButtonPressedFirst() {
        runBlocking {
            form.okButton.activate()
            assertEquals("ok", form.execute())
        }
    }

    @Test
    fun cancelButtonPressedFirst() {
        runBlocking {
            form.cancelButton.activate()
            assertEquals("cancel", form.execute())
        }
    }

    @Test
    fun okButtonPressedLast() {
        var state = 0
        var result = ""

        runBlocking {
            var waitTask = launch {
                assertEquals(0, state++)
                result = form.execute()
                assertEquals(2, state++)
            }

            yield()

            assertEquals(1, state++)
            form.okButton.activate()
            waitTask.join()
            assertEquals(3, state++)
        }

        assertEquals("ok", result)
    }
}
