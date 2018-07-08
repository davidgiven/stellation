package runtime.js

import interfaces.IDatastore
import interfaces.ILogger
import interfaces.ITime
import runtime.shared.InMemoryDatastore
import utils.bind

fun initJsRuntime() {
    bind<ILogger>(JsLogger())
    bind<ITime>(JsTime())
    bind<IDatastore>(InMemoryDatastore())
}
