package server.jvm

import runtime.jvm.initJvmRuntime

fun main(argv: Array<String>) {
    initJvmRuntime()
    server.main(argv)
}

