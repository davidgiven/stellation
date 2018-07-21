package ui

import interfaces.IUi
import interfaces.IUiElement

abstract class AbstractWindow(val ui: IUi) {
    lateinit var element: IUiElement

    abstract fun createTitlebar(div: IUiElement)
    abstract fun createUserInterface(div: IUiElement)

    open fun create() {
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
        }
    }

}

fun <T : AbstractWindow> T.show(): T {
    create()
    return this
}
