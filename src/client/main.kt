package client

import interfaces.ITime
import interfaces.hourstime
import model.Model
import runtime.js.initJsRuntime
import utils.bind
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.html.dom.append
import kotlinx.html.p
import utils.get
import kotlin.browser.document
import org.w3c.dom.Document
import ui.LoginForm

fun main(argv: Array<String>) {
    initJsRuntime()
    bind(Model())

    async {
        while (true) {
            println("The current time is ${get<ITime>().hourstime()}.")
//            val newElement = document.getElementById("email") as Element
            val d: Document = document
            d.getElementById("new")!!.append {
                p {
                    +"Some text! ${d.URL}"
                }
            }
            delay(1000)
        }
    }
}

