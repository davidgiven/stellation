package runtime.js

import interfaces.ILogger

class JsLogger : ILogger {
    override fun println(message: String) {
        kotlin.io.println(message)
    }
}
