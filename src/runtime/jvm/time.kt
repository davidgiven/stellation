package runtime.jvm

import interfaces.ITime

class JvmTime : ITime {
    override fun realtime() = System.nanoTime().toDouble() / 1e9
}
