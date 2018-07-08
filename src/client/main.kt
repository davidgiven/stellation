package client

import interfaces.ITime
import interfaces.hourstime
import model.Model
import runtime.js.initJsRuntime
import utils.bind
import utils.get

fun main(argv: Array<String>) {
    initJsRuntime()
    bind(Model())
    println("The current time is ${get<ITime>().hourstime()}.")
}

