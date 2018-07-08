package runtime.js

import interfaces.ITime
import kotlin.js.Date

class JsTime : ITime {
    override fun nanotime() = (Date().getTime() * 1e6).toLong()
}
