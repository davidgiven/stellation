package runtime.konan

import interfaces.ITime

class KonanTime : ITime {
    override fun realtime() = kotlin.system.getTimeNanos().toDouble() / 1e9
}
