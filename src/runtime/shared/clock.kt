package runtime.shared

import interfaces.IClock

class Clock : IClock {
    private var time: Double = 0.0

    override fun setTime(time: Double) {
        this.time = time
    }

    override fun getTime() = time
}
