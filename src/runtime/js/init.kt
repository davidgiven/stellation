package runtime.js

import interfaces.IContext
import interfaces.ILogger
import interfaces.ITime
import interfaces.context

fun initJsRuntime() {
    context = object : IContext() {
        override val logger: ILogger? = JsLogger()
        override val time: ITime? = JsTime()
    }
}
