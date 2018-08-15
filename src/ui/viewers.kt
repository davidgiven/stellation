package ui

import interfaces.IUiElement
import model.NAME
import model.SThing
import model.onPropertyChangedGlobalEvent

fun IUiElement.addNameViewer(thing: SThing) {
    addElement("span") {
        val span = addText("span", "")

        fun update() {
            span.text = thing.name
        }

        onPropertyChangedGlobalEvent(thing, NAME, ::update)
        update()
    }
}
