package client

import interfaces.ITime
import interfaces.hourstime
import kotlinx.coroutines.experimental.async
import model.Model
import org.w3c.dom.Element
import runtime.js.initJsRuntime
import ui.LoginForm
import ui.show
import utils.bind
import utils.get
import kotlin.browser.document

fun main(argv: Array<String>) {
    initJsRuntime()
    bind(Model())

    async {
        println("The current time is ${get<ITime>().hourstime()}.")

        document.body!!.removeChildren()
        val form = LoginForm().show()
        println("The form said ${form.execute()}!")

//            val newElement = document.getElementById("email") as Element
//            val d: Document = document
//            d.getElementById("new")!!.append {
//                p {
//                    +"Some text! ${d.URL}"
//                }
//            }
//            delay(1000)
    }
}

fun Element.removeChildren(): Element {
    while (lastChild != null) {
        removeChild(lastChild!!)
    }
    return this
}