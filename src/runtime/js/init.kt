package runtime.js

import interfaces.ILogger
import interfaces.ITime
import utils.bind

fun initJsRuntime() {
    bind<ILogger>(JsLogger())
    bind<ITime>(JsTime())
}
