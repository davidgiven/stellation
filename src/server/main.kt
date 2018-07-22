package server

import interfaces.IEnvironment
import utils.get

fun main(argv: Array<String>) {
    val environment = get<IEnvironment>()
    if (environment.getenv("GATEWAY_INTERFACE") != null) {
        serveCgi()
    } else {
        serveCli(argv)
    }
}
