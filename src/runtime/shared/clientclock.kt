package runtime.shared

import interfaces.IClock
import interfaces.ITime
import interfaces.log
import utils.injection

class ClientClock : IClock {
    val time by injection<ITime>()

    var lastServerTime = 0.0
    var lastClientTimeAtSync = 0.0

    override fun setTime(serverTime: Double) {
        if (lastServerTime != 0.0) {
            val estimatedServerTime = getTime()
            log("clock skew: ${estimatedServerTime - serverTime}")
        }

        lastServerTime = serverTime
        lastClientTimeAtSync = time.realtime()
    }

    override fun getTime(): Double {
        val deltaSinceSync = time.realtime() - lastClientTimeAtSync
        return lastServerTime + deltaSinceSync
    }
}
