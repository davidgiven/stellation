package model

import datastore.Oid
import datastore.doesObjectExist
import kotlin.reflect.KClass

class ObjectNotVisibleException(val oid: Oid)
    : Exception("object $oid does not exist or is not visible")

class DatabaseTypeMismatchException(val oid: Oid, val kind: String, val desired: String)
    : Exception("expected $oid to be a $desired, but it was a $kind")

private typealias ClassMap = Map<String, (Oid) -> SThing>

var classes: ClassMap = emptyMap<String, (Oid) -> SThing>()
        .registerClass(::SAntimatterDistillery)
        .registerClass(::SAsteroidMiner)
        .registerClass(::SCargo)
        .registerClass(::SFactory)
        .registerClass(::SFrame)
        .registerClass(::SGalaxy)
        .registerClass(::SHydroponicsPlant)
        .registerClass(::SJumpdrive)
        .registerClass(::SModule)
        .registerClass(::SPlayer)
        .registerClass(::SRAMCannon)
        .registerClass(::SRefinery)
        .registerClass(::SShip)
        .registerClass(::SStar)
        .registerClass(::STank)
        .registerClass(::SUniverse)
        .registerClass(::SWeapon)

inline fun <reified T : SThing> ClassMap.registerClass(noinline constructor: (Oid) -> T): ClassMap =
        this + Pair(T::class.simpleName!!, constructor)

private var objectCache = emptyMap<Oid, SThing>()

fun resetObjectCache() {
    objectCache = emptyMap()
}

@Suppress("UNCHECKED_CAST")
fun <T : SThing> loadRawObject(oid: Oid, kclass: KClass<T>): T {
    if (!doesObjectExist(oid)) {
        throw ObjectNotVisibleException(oid)
    }

    val kind = KIND.get(oid).get()
    val instance: SThing = classes.getValue(kind)(oid)
    if (kclass.isInstance(instance)) {
        return instance as T
    }
    throw DatabaseTypeMismatchException(oid, kind, kclass.simpleName!!)
}

@Suppress("UNCHECKED_CAST")
fun <T : SThing> loadObject(oid: Oid, kclass: KClass<T>): T {
    var instance = objectCache.get(oid)
    if (instance == null) {
        instance = loadRawObject(oid, kclass)
        objectCache += oid to instance
    }

    if (kclass.isInstance(instance)) {
        return instance as T
    }
    throw DatabaseTypeMismatchException(oid, KIND.get(oid).get(), kclass.simpleName!!)
}

fun <T : SThing> createObject(klass: KClass<T>): T {
    val oid = datastore.createObject()
    KIND.get(oid).set(klass.simpleName!!)
    return oid.load(klass)
}

fun <T : SThing> Oid.load(klass: KClass<T>): T = loadObject(this, klass)
fun <T : SThing> Oid.loadRaw(klass: KClass<T>): T = loadRawObject(this, klass)
