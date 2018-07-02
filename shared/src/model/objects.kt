package model

import datastore.AggregateProperty
import datastore.Oid
import datastore.PrimitiveProperty
import datastore.doesObjectExist
import kotlin.reflect.KClass

class ObjectNotVisibleException(val oid: Oid)
    : Exception("object $oid does not exist or is not visible")

class DatabaseTypeMismatchException(val oid: Oid, val kind: String, val desired: String)
    : Exception("expected $oid to be a $desired, but it was a $kind")

private typealias ClassMap = Map<String, (Oid) -> SThing>

var classes: ClassMap = emptyMap<String, (Oid) -> SThing>()
        .registerClass(::SCargo)
        .registerClass(::SFactory)
        .registerClass(::SFrame)
        .registerClass(::SGalaxy)
        .registerClass(::SJumpdrive)
        .registerClass(::SModule)
        .registerClass(::SPlayer)
        .registerClass(::SRAMCannon)
        .registerClass(::SShip)
        .registerClass(::SStar)
        .registerClass(::STank)
        .registerClass(::SUniverse)
        .registerClass(::SWeapon)

inline fun <reified T : SThing> ClassMap.registerClass(noinline constructor: (Oid) -> T): ClassMap =
        this + Pair(T::class.simpleName!!, constructor)

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
    protected fun <T> aggregate(property: AggregateProperty<T>) = property.get(oid)

    var kind by primitive(KIND)
    var owner by primitive(OWNER)
}

@Suppress("UNCHECKED_CAST")
fun <T : SThing> loadObject(oid: Oid, klass: KClass<T>): T {
    if (!doesObjectExist(oid)) {
        throw ObjectNotVisibleException(oid)
    }

    val kind = KIND.get(oid).get()
    val instance: SThing = classes.getValue(kind)(oid)
    if (klass.isInstance(instance)) {
        return instance as T
    }
    throw DatabaseTypeMismatchException(oid, kind, klass.simpleName!!)
}

fun <T : SThing> createObject(klass: KClass<T>): T {
    val oid = datastore.createObject()
    KIND.get(oid).set(klass.simpleName!!)
    return oid.load(klass)
}

fun <T : SThing> Oid.load(klass: KClass<T>): T = loadObject(this, klass)

