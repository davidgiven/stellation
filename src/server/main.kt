package server

import interfaces.context
import interfaces.hourstime

fun main(argv: Array<String>) {
    println("The current time on the server is ${context().time!!.hourstime()}.")
}

