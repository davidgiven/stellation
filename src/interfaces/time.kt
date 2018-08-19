package interfaces

interface ITime {
    /* Returns seconds since epoch */
    fun realtime(): Double

    fun formatTime(t: Double): String
}

fun ITime.millitime() = (this.realtime() * 1000.0).toLong()
fun ITime.hourstime() = this.realtime() / 3600.0
fun ITime.nanotime() = (this.realtime() * 1e9).toLong()
