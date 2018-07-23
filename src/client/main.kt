package client

import model.Model
import org.w3c.dom.Element
import runtime.js.initJsRuntime
import runtime.js.kickScheduler
import utils.bind
import utils.Codec
import kotlin.browser.document
import interfaces.IConsole

fun main(argv: Array<String>) {
    initJsRuntime()
    bind(Codec())
    bind(Model())

    document.body!!.removeChildren()
    val console = bind<IConsole>(Console().show())

    startGame()
    kickScheduler()
}

fun Element.removeChildren(): Element {
    while (lastChild != null) {
        removeChild(lastChild!!)
    }
    return this
}