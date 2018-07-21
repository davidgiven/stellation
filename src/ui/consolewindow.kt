package ui

import interfaces.IUiElement
import interfaces.IUi
import utils.get

typealias ConsoleCommandCallback = (String) -> Unit

class ConsoleWindow(ui: IUi = get(), val callback: ConsoleCommandCallback) : AbstractWindow(ui) {
    lateinit var linesBox: IUiElement
    lateinit var textInput: IUiElement

    override fun createTitlebar(div: IUiElement) {
        div.addText("div", "Console")
    }

    override fun createUserInterface(div: IUiElement) {
        div.classes += "console"

        div.addVBox {
            linesBox = div.addVBox {
            }
            textInput = div.addElement("input") {
                set("type", "text")

                onActivate {
                    val value = textInput["value"]!!
                    textInput["value"] = ""
                    callback(value)
                }
            }
        }
    }

    fun print(s: String) {
        linesBox.addText("div", s).classes
    }
}

