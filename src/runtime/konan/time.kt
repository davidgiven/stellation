package runtime.konan

import interfaces.ITime

class KonanTime : ITime {
    override fun realtime() = kotlin.system.getTimeNanos().toDouble() / 1e9

    override fun formatTime(t: Double): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
