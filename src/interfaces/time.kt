package interfaces

interface Time {
    fun nanotime(): Long
}

fun Time.millitime() = this.nanotime() / 1000L
fun Time.hourstime() = this.nanotime().toDouble() / 1e6
