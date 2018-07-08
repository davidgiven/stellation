package client

import interfaces.ITime
import interfaces.hourstime
import runtime.js.initJsRuntime
import utils.get

fun main(argv: Array<String>) {
    initJsRuntime()
    println("The current time is ${get<ITime>().hourstime()}.")
}

