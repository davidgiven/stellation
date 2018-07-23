package runtime.js

import interfaces.IDatastore
import interfaces.ILogger
import interfaces.ITime
import interfaces.IUi
import interfaces.IUtf8
import runtime.shared.InMemoryDatastore
import utils.bind

fun initJsRuntime() {
    bind<IUtf8>(JsUtf8())
    bind<ILogger>(JsLogger())
    bind<ITime>(JsTime())
    bind<IDatastore>(InMemoryDatastore())
    bind<IUi>(JsUi())
}
