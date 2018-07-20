package ui

import interfaces.IUi
import interfaces.IUiElement
import kotlinx.coroutines.experimental.channels.Channel

data class YesNoButtons(val yesButton: IUiElement, val noButton: IUiElement)

abstract class AbstractForm<T>(val ui: IUi) {
    val data = Channel<T>(1)
    var finished: Boolean = false

    var element: IUiElement? = null

    abstract fun createTitlebar(div: IUiElement)
    abstract fun createUserInterface(div: IUiElement)
    abstract fun createButtonBox(div: IUiElement)

    fun create() {
        element = ui.newModal {
            classes = setOf("form")


            addElement("div") {
                classes = setOf("titlebar")
                createTitlebar(this)
            }

            addElement("div") {
                classes = setOf("body")
                createUserInterface(this)
            }

            addElement("div") {
                classes = setOf("buttonbox")
                createButtonBox(this)
            }
        }
    }

    suspend fun post(value: T) {
        data.send(value)
    }

    suspend fun execute(): T = data.receive()

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

fun <T : AbstractForm<*>> T.show(): T {
    create()
    return this
}
