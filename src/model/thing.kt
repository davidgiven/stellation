package model

import utils.Oid
import utils.Fault
import utils.FaultDomain.INVALID_ARGUMENT
import utils.injection

fun throwRecursiveMoveException(child: Oid): Nothing =
        throw Fault(INVALID_ARGUMENT).withDetail("$child cannot be contained by itself")


abstract class SThing(val model: Model, val oid: Oid): Iterable<SThing> {
    var kind by KIND
    var owner by OWNER
    var location by LOCATION
    val contents by CONTENTS
    var name by NAME

    private val timers by injection<Timers>()

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if ((other == null) || (other !is SThing))
            return false
        return oid == other.oid
    }

    override fun hashCode(): Int = oid

    override fun iterator(): Iterator<SThing> = contents.iterator()

    open fun onTimerExpiry() {
    }

    open fun recomputeTimeout(): Double? {
        return null
    }

    open fun computeVisibilitySet(): Set<SThing> {
        var set = emptySet<SThing>()
        for (o in contents) {
            set += o
            set += o.computeVisibilitySet()
        }
        return set
    }

    fun kickTimer() {
        val expiry = recomputeTimeout()
        if (expiry != null) {
            timers.setTimer(oid, expiry)
        }
    }
}

inline fun <reified C : SThing> SThing.findChild(): C? =
        contents.find { it is C } as C?

fun SThing.getContainingStar(): SStar? {
    var loc: SThing? = this
    while (loc != null) {
        if (loc is SStar) {
            return loc
        }
        loc = loc.location
    }
    return null
}

fun SThing.calculateHierarchicalContents(): Set<SThing> =
        model
                .datastore
                .getHierarchy(oid, "contents")
                .map { model.loadObject(it, SThing::class) }
                .toSet()

fun <T : SThing> T.moveTo(destination: SThing): T {
    var loc: SThing? = destination
    while (loc != null) {
        if (loc == this) {
            throwRecursiveMoveException(this.oid)
        }
        loc = loc.location
    }

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

