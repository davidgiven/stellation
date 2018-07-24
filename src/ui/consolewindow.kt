package ui

import interfaces.IUi
import interfaces.IUiElement
import utils.get

typealias ConsoleCommandCallback = (String) -> Unit

class ConsoleWindow(ui: IUi = get(), val callback: ConsoleCommandCallback) : AbstractWindow(ui) {
    lateinit var linesBox: IUiElement
    lateinit var textInput: IUiElement

    override val mainClass = "consoleWindow"
    override val isResizable = false

    override fun createTitlebar(div: IUiElement) {
        div.addText("span", "Console")
    }

    override fun createUserInterface(div: IUiElement) {
        div.classes += "console"

        div.addElement("label") {
            classes += setOf("expand")

            linesBox = addElement("div") {
                classes += setOf("lines")
            }

            addHBox {
                addText("div", ">") {
                    classes += "prompt"
                }

                textInput = addElement("input") {
                    set("type", "text")

                    onActivate {
                        val value = textInput["value"]!!
                        textInput["value"] = ""
                        callback(value)
                    }
                }
            }

            addElement("div") {
                classes += "expand"
            }
        }
    }

    fun print(s: String) {
        linesBox.addText("div", s).classes
        textInput.scrollIntoView()
    }
}

