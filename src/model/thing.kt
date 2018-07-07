package model

import datastore.Oid

abstract class SThing(val oid: Oid) {
    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if ((other == null) || (other !is SThing))
            return false
        return oid == this.oid
    }

    override fun hashCode(): Int = oid

    protected fun <T> primitive(property: PrimitiveProperty<T>) = property.get(oid)
    protected fun <T : SThing> aggregate(property: SetProperty<T>) = property.get(oid)

    var kind by primitive(KIND)
    var owner by primitive(OWNER)
    var location by primitive(LOCATION)
    val contents by aggregate(CONTENTS)

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

