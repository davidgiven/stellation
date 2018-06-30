package runtime

expect fun println(vararg ss: String)
expect fun exit(status: Int)
expect fun nanotime(): Long
