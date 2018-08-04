package runtime.js

import interfaces.ITime
import kotlin.js.Date

class JsTime : ITime {
    override fun realtime() = Date().getTime()
}
