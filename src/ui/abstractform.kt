package ui

import interfaces.IUi
import interfaces.IUiElement
import utils.Mailbox

data class YesNoButtons(val yesButton: IUiElement, val noButton: IUiElement)

abstract class AbstractForm<T>(ui: IUi) : AbstractWindow(ui) {
    val mailbox = Mailbox<T>()
    var finished: Boolean = false

    abstract fun createButtonBox(div: IUiElement)

    override fun create() {
        super.create()
        element.classes += "form"
        element.addElement("div") {
            classes = setOf("buttonbox")
            createButtonBox(this)
        }
    }

    fun post(value: T) {
        mailbox.post(value)
    }

    suspend fun execute(): T = mailbox.wait()

    fun createOkButtonBox(div: IUiElement) =
            div.addElement("button") {
                classes = setOf("button", "defaultbutton")
                addText("span", "OK")
            }

    fun createYesNoButtonBox(div: IUiElement, yes: String = "Yes", no: String = "No"): YesNoButtons {
        val yesButton = div.addElement("button") {
            classes = setOf("button", "defaultbutton")
            addText("span", yes)
        }

        val noButton = div.addElement("button") {
            classes = setOf("button", "cancelbutton")
            addText("span", no)
        }

        return YesNoButtons(yesButton, noButton)
    }
}
