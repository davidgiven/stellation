package server

import interfaces.context
import interfaces.hourstime

fun main(argv: Array<String>) {
    val time = context.time!!
    println("The current time on the server is ${time.hourstime()}.")
}

