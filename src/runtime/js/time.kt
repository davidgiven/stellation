package runtime.js

import interfaces.ITime
import kotlin.js.Date

class JsTime : ITime {
    override fun realtime() = Date().getTime() / 1000.0

    override fun formatTime(t: Double): String {
        val jstime = Date(t * 1000.0)
        return "${jstime.toLocaleString()}"
    }
}
