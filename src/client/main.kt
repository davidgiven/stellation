package client

import interfaces.context
import interfaces.hourstime
import runtime.js.initJsRuntime


fun main(argv: Array<String>) {
    initJsRuntime()
    println("The current time is ${context().time!!.hourstime()}.")
}

