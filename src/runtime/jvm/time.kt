package runtime.jvm

import interfaces.Time

class JvmTime : Time {
    override fun nanotime(): Long = System.nanoTime()
}
