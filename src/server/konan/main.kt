package server.konan

import runtime.konan.initKonanRuntime

fun main(argv: Array<String>) {
    initKonanRuntime()
    server.main(argv)
}

