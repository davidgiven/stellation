package client

import model.Model
import org.w3c.dom.Element
import runtime.js.initJsRuntime
import runtime.js.kickScheduler
import utils.bind
import kotlin.browser.document

fun main(argv: Array<String>) {
    initJsRuntime()
    bind(Model())

    document.body!!.removeChildren()
    startGame()
    kickScheduler()
}

fun Element.removeChildren(): Element {
    while (lastChild != null) {
        removeChild(lastChild!!)
    }
    return this
}