package ui

import interfaces.IUi
import interfaces.IUiElement
import runtime.jvm.JvmStubUi
import utils.Job
import utils.bind
import utils.hasRunnableJobs
import utils.resetBindingsForTest
import utils.schedule
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TestForm: AbstractForm<String>() {
    lateinit var okButton: IUiElement
    lateinit var cancelButton: IUiElement

    override val mainClass: String = "mainClass"

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
    var form: TestForm? = null
    var testRan = false

    @BeforeTest
    fun setup() {
        resetBindingsForTest()
        bind<IUi>(JvmStubUi())
        form = TestForm().show()
    }

    @AfterTest
    fun teardown() {
        runRunnableJobs()
        assertEquals(true, testRan)
    }

    @Test
    fun simplePost() {
        Job {
            form!!.post("fnord")
            assertEquals("fnord", form!!.execute())
            testRan = true
        }
    }

    @Test
    fun okButtonPressedFirst() {
        form!!.okButton.activate()
        Job {
            assertEquals("ok", form!!.execute())
            testRan = true
        }
    }

    @Test
    fun cancelButtonPressedFirst() {
        form!!.cancelButton.activate()
        Job {
            assertEquals("cancel", form!!.execute())
            testRan = true
        }
    }

    @Test
    fun okButtonPressedLast() {
        Job {
            assertEquals("ok", form!!.execute())
            testRan = true
        }
        form!!.okButton.activate()
    }

    private fun runRunnableJobs() {
        while (hasRunnableJobs()) {
            schedule()
        }
    }
}
