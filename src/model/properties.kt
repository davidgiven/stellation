@file:Suppress("NOTHING_TO_INLINE")

package model

import utils.Oid
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

enum class Scope {
    SERVERONLY,
    PRIVATE,
    LOCAL,
    GLOBAL,
}

interface Aggregate<T : SThing> : Iterable<T> {
    fun add(item: T): Aggregate<T>
    fun remove(item: T): Aggregate<T>
    fun clear(): Aggregate<T>
    operator fun contains(item: T): Boolean
    fun getOne(): T?
    fun replaceAll(items: Iterable<T>): Aggregate<T>

    fun getAll(): Set<T> = iterator().toSet()
    fun forEach(action: (T) -> Unit) = iterator().forEach(action)

    fun addAll(iterable: Iterable<T>): Aggregate<T> {
        iterable.forEach { add(it) }
        return this
    }

    operator fun plusAssign(item: T) {
        add(item)
    }

    operator fun minusAssign(item: T) {
        remove(item)
    }
}

interface VarProxy<T> : ReadWriteProperty<SThing, T>
interface ValProxy<T> : ReadOnlyProperty<SThing, T>

var allProperties: Map<String, Property> = emptyMap()

abstract class Property(val scope: Scope, val name: String) {
    init {
        allProperties += name to this
    }

    abstract val sqlType: String
    abstract val isAggregate: Boolean

    fun hasValue(model: Model, oid: Oid) = model.datastore.hasProperty(oid, name)
    fun hasValue(thisRef: SThing, property: KProperty<*>) = hasValue(thisRef.model, thisRef.oid)

    abstract fun serialiseToString(model: Model, oid: Oid): String
    abstract fun deserialiseFromString(model: Model, oid: Oid, value: String)
}

abstract class PrimitiveProperty<T>(scope: Scope, name: String)
    : Property(scope, name), VarProxy<T> {
    protected open fun getPrimitive(model: Model, oid: Oid): T = TODO("get get $name yet")
    protected open fun setPrimitive(model: Model, oid: Oid, value: T): Unit = TODO("can't set $name yet")

    override fun getValue(thisRef: SThing, property: KProperty<*>): T =
            getPrimitive(thisRef.model, thisRef.oid)

    override fun setValue(thisRef: SThing, property: KProperty<*>, value: T) {
        setPrimitive(thisRef.model, thisRef.oid, value)
    }

    override fun serialiseToString(model: Model, oid: Oid) =
            getPrimitive(model, oid).toString()

    override val isAggregate = false
}

open class IntProperty(scope: Scope, name: String) : PrimitiveProperty<Int>(scope, name) {
    override val sqlType = "INTEGER"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getIntProperty(oid, name)

    override fun setPrimitive(model: Model, oid: Oid, value: Int) =
            model.datastore.setIntProperty(oid, name, value)

    override fun deserialiseFromString(model: Model, oid: Oid, value: String) =
            setPrimitive(model, oid, value.toInt())
}

open class LongProperty(scope: Scope, name: String) : PrimitiveProperty<Long>(scope, name) {
    override val sqlType = "INTEGER"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getLongProperty(oid, name)

    override fun setPrimitive(model: Model, oid: Oid, value: Long) =
            model.datastore.setLongProperty(oid, name, value)

    override fun deserialiseFromString(model: Model, oid: Oid, value: String) =
            setPrimitive(model, oid, value.toLong())
}

open class RealProperty(scope: Scope, name: String) : PrimitiveProperty<Double>(scope, name) {
    override val sqlType = "REAL"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getRealProperty(oid, name)

    override fun setPrimitive(model: Model, oid: Oid, value: Double) =
            model.datastore.setRealProperty(oid, name, value)

    override fun deserialiseFromString(model: Model, oid: Oid, value: String) =
            setPrimitive(model, oid, value.toDouble())
}

open class StringProperty(scope: Scope, name: String) : PrimitiveProperty<String>(scope, name) {
    override val sqlType = "TEXT"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getStringProperty(oid, name)

    override fun setPrimitive(model: Model, oid: Oid, value: String) =
            model.datastore.setStringProperty(oid, name, value)

    override fun deserialiseFromString(model: Model, oid: Oid, value: String) =
            setPrimitive(model, oid, value)
}

open class RefProperty<T : SThing>(scope: Scope, name: String, val kclass: KClass<T>) :
        PrimitiveProperty<T?>(scope, name) {
    override val sqlType = "INTEGER REFERENCES objects(oid) ON DELETE CASCADE"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getOidProperty(oid, name)?.load(model, kclass)

    override fun setPrimitive(model: Model, oid: Oid, value: T?) =
            model.datastore.setOidProperty(oid, name, value?.oid)

    override fun serialiseToString(model: Model, oid: Oid) =
            model.datastore.getOidProperty(oid, name).toString()

    override fun deserialiseFromString(model: Model, oid: Oid, value: String) =
            model.datastore.setOidProperty(oid, name, value.toInt())
}

open class SetProperty<T : SThing>(scope: Scope, name: String, val kclass: KClass<T>)
    : Property(scope, name), ValProxy<Aggregate<T>> {
    override val sqlType = "INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE"
    override val isAggregate = true

    override fun serialiseToString(model: Model, oid: Oid) =
            model.datastore.getSetProperty(oid, name).getAll().map { it.toString() }.joinToString(",")

    override fun deserialiseFromString(model: Model, oid: Oid, value: String) {
        val values = value.split(',').map { it.toInt() }
        model.datastore.getSetProperty(oid, name).clear().replaceAll(values)
    }

    override fun getValue(thisRef: SThing, property: KProperty<*>): Aggregate<T> {
        val model = thisRef.model
        val oid = thisRef.oid
        val set = model.datastore.getSetProperty(oid, name)

        return object : Aggregate<T> {
            override fun add(item: T): Aggregate<T> {
                set.add(item.oid)
                return this
            }

            override fun remove(item: T): Aggregate<T> {
                set.remove(item.oid)
                return this
            }

            override fun clear(): Aggregate<T> {
                set.clear()
                return this
            }

            override fun contains(item: T): Boolean = set.contains(item.oid)

            override fun iterator(): Iterator<T> =
                    set.getAll().map { it.load(model, kclass) }.iterator()

            override fun getOne(): T? = set.getOne()?.load(model, kclass)

            override fun replaceAll(items: Iterable<T>): Aggregate<T> {
                val oldOids = set.getAll()
                val newOids = items.map { it.oid }

                for (o in oldOids) {
                    if (!newOids.contains(o)) {
                        set.remove(o)
                    }
                }

                for (o in newOids) {
                    if (!oldOids.contains(o)) {
                        set.add(o)
                    }
                }

                return this
            }
        }
    }
}

private inline fun <T> Iterator<T>.toSet() =
        HashSet<T>().apply {
            while (hasNext()) {
                this += next()
            }
        }

val BRIGHTNESS = RealProperty(Scope.LOCAL, "brightness")
val CONTENTS = SetProperty(Scope.LOCAL, "contents", SThing::class)
val DATA = StringProperty(Scope.LOCAL, "data")
val EMAIL_ADDRESS = StringProperty(Scope.SERVERONLY, "email_address")
val GALAXY = RefProperty(Scope.LOCAL, "galaxy", SGalaxy::class)
val KIND = StringProperty(Scope.LOCAL, "kind")
val LOCATION = RefProperty(Scope.LOCAL, "location", SThing::class)
val NAME = StringProperty(Scope.LOCAL, "name")
val OWNER = RefProperty(Scope.LOCAL, "owner", SPlayer::class)
val PASSWORD_HASH = StringProperty(Scope.SERVERONLY, "password_hash")
val PLAYERS = SetProperty(Scope.SERVERONLY, "players", SPlayer::class)
val SHIPS = SetProperty(Scope.PRIVATE, "ships", SShip::class)
val VISIBLE_OBJECTS = SetProperty(Scope.PRIVATE, "visible_objects", SThing::class)
val XPOS = RealProperty(Scope.LOCAL, "x")
val YPOS = RealProperty(Scope.LOCAL, "y")

