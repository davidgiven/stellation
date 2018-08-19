package runtime.jvm

import interfaces.ITime
import java.text.DateFormat
import java.text.DateFormat.LONG
import java.util.*

class JvmTime : ITime {
    val formatter = DateFormat.getDateTimeInstance(LONG, LONG)

    override fun realtime() = System.nanoTime().toDouble() / 1e9

    override fun formatTime(t: Double): String {
        val date = Date((t * 1000.0).toLong())
        return formatter.format(date)
    }
}
