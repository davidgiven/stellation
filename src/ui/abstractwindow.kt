package ui

import interfaces.IUi
import interfaces.IUiElement

abstract class AbstractWindow(val ui: IUi) {
    lateinit var element: IUiElement

    abstract fun createTitlebar(div: IUiElement)
    abstract fun createUserInterface(div: IUiElement)

    open fun create() {
        element = ui.newModal {
            classes = setOf("window", "vbox")

            addElement("div") {
                classes = setOf("titlebar")
                addHBox {
                    createTitlebar(this)

                    addElement("span") {
                        classes = setOf("expand", "textured")
                    }
                }
            }

            addElement("div") {
                classes = setOf("body", "expand", "vbox")
                createUserInterface(this)
            }
        }
    }

}

fun <T : AbstractWindow> T.show(): T {
    create()
    return this
}
