package model

import interfaces.Oid

abstract class SThing(val model: Model, val oid: Oid) {
    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if ((other == null) || (other !is SThing))
            return false
        return oid == this.oid
    }

    override fun hashCode(): Int = oid

    var kind by KIND
    var owner by OWNER
    var location by LOCATION
    val contents by CONTENTS

    open fun onTimerExpiry(time: Long) {
    }

    open fun recomputeTimeout(): Long? {
        return null
    }

//    fun kickTimer() {
//        val expiry = recomputeTimeout()
//        if (expiry != null) {
//            setTimer(oid, expiry)
//        }
//    }
}

fun <T : SThing> T.moveTo(destination: SThing): T {
    this.remove()
    this.location = destination
    destination.contents += this
    return this
}

fun <T : SThing> T.remove(): T {
    this.location?.contents?.remove(this)
    this.location = null
    return this
}

