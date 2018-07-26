package ui

import interfaces.IUi
import interfaces.IUiElement
import interfaces.UiDragCallbacks
import utils.injection
import kotlin.math.max

abstract class AbstractWindow {
    val ui by injection<IUi>()

    lateinit var element: IUiElement
    lateinit var titlebar: IUiElement
    lateinit var resizer: IUiElement

    abstract val mainClass: String
    open val isResizable = true

    private var dragCallbacks = object : UiDragCallbacks {
        var startX: Int = 0
        var startY: Int = 0

        override fun onStart(x: Int, y: Int) {
            val (winX, winY) = element.getPosition()
            startX = winX - x
            startY = winY - y
        }

        override fun onMove(x: Int, y: Int) {
            val newX = startX + x
            val newY = startY + y
            element.setPosition(newX, newY)
        }

        override fun onEnd(x: Int, y: Int) {
            onMove(x, y)
        }
    }

    private var resizeCallbacks = object : UiDragCallbacks {
        var startX: Int = 0
        var startY: Int = 0

        override fun onStart(x: Int, y: Int) {
            val (winX, winY) = element.getSize()
            startX = winX - x
            startY = winY - y
        }

        override fun onMove(x: Int, y: Int) {
            val newW = max(startX + x, 128)
            val newH = max(startY + y, 128)
            element.setSize(newW, newH)
        }

        override fun onEnd(x: Int, y: Int) {
            onMove(x, y)
        }
    }

    abstract fun createTitlebar(div: IUiElement)
    abstract fun createUserInterface(div: IUiElement)

    open fun create() {
        element = ui.newModal {
            classes = setOf("window", "vbox", mainClass)

            titlebar = addElement("div") {
                classes = setOf("titlebar")
                addHBox {
                    createTitlebar(this)

                    addElement("span") {
                        classes = setOf("expand", "textured")
                    }
                }

                onDrag(dragCallbacks)
            }

            addElement("div") {
                classes = setOf("body", "expand", "vbox")
                createUserInterface(this)
            }

            if (isResizable) {
                addElement("div") {
                    classes = setOf("resizer")

                    onDrag(resizeCallbacks)
                }
            }
        }
    }
}

fun <T : AbstractWindow> T.show(): T {
    create()
    return this
}

fun <T : AbstractWindow> T.hide(): T {
    element.remove()
    return this
}
