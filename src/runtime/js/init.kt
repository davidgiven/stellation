package runtime.js

import interfaces.IContext

fun initJsRuntime() {
    IContext.context = object : IContext() {
        override val logger = JsLogger()
        override val time = JsTime()
    }
}
