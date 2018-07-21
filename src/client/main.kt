package client

import interfaces.ITime
import interfaces.hourstime
import model.Model
import org.w3c.dom.Element
import runtime.js.initJsRuntime
import runtime.js.kickScheduler
import utils.Job
import utils.bind
import utils.get
import kotlin.browser.document

fun main(argv: Array<String>) {
    initJsRuntime()
    bind(Model())

    Job {
        println("The current time is ${get<ITime>().hourstime()}.")

        document.body!!.removeChildren()
        val console = Console().show()

        Job {
            for (i in 0..10) {
                console.println("I can do it ${i} times!")
                suspend()
            }
        }
    }
    kickScheduler()
}

fun Element.removeChildren(): Element {
    while (lastChild != null) {
        removeChild(lastChild!!)
    }
    return this
}