package interfaces

interface ITime {
    fun nanotime(): Long
}

fun ITime.millitime() = this.nanotime() / 1000L
fun ITime.hourstime() = this.nanotime().toDouble() / 1e6 / 3600.0
