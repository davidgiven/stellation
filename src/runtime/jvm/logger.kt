package runtime.jvm

import interfaces.ILogger

class JvmLogger : ILogger {
    override fun println(message: String) {
        System.err.println(message)
    }
}
