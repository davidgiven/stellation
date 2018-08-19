package runtime.shared

import interfaces.IClock

class ServerClock : IClock {
    private var time: Double = 0.0

    override fun setTime(serverTime: Double) {
        this.time = serverTime
    }

    override fun getTime() = time
}
