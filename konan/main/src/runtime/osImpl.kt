package runtime

actual fun println(vararg ss: String) {
    for (s in ss) {
        kotlin.io.print(s)
    }
    kotlin.io.println()
}

actual fun exit(status: Int) {
    platform.posix.exit(status)
}

actual fun nanotime() = kotlin.system.getTimeNanos()

