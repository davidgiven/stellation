package runtime

actual fun println(vararg ss: String) {
    for (s in ss) {
        System.out.print(s)
    }
    System.out.println()
}

actual fun exit(status: Int) {
    System.exit(status)
}

actual fun nanotime() = System.nanoTime()


