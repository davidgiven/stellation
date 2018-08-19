package ui

import interfaces.ITime
import interfaces.IUiElement
import interfaces.onTickGlobalEvent
import model.NAME
import model.SThing
import model.onPropertyChangedGlobalEvent
import utils.Fault
import utils.FaultDomain.VISIBILITY
import utils.injection

fun IUiElement.addNameViewer(thing: SThing) {
    addElement("span") {
        val span = addText("span", "")

        fun update() {
            try {
                span.text = thing.name
            } catch (e: Fault) {
                if (e.domain == VISIBILITY) {
                    span.text = "(not reachable)"
                } else {
                    throw e
                }
            }
        }

        onPropertyChangedGlobalEvent(thing, NAME, ::update)
        update()
    }
}

fun IUiElement.addTimeViewer(provider: ()->Double) {
    val time by injection<ITime>()

    addElement("span") {
        val span = addText("span", "")

        fun update() {
            val now = provider()
            span.text = renderTime(now)

            val formatted = time.formatTime(now)
            set("title", "$formatted")
        }

        onTickGlobalEvent(::update)
        update()
    }
}
