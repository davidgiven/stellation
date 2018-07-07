package runtime.jvm

import interfaces.ITime

class JvmTime : ITime {
    override fun nanotime(): Long = System.nanoTime()
}
