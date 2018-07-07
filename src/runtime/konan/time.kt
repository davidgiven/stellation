package runtime.konan

import interfaces.ITime

class KonanTime : ITime {
    override fun nanotime(): Long = kotlin.system.getTimeNanos()
}
