package model

import interfaces.Oid
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
    fun getAll(): List<T>
    fun getOne(): T?

    fun forEach(action: (T) -> Unit) = getAll().forEach(action)
    override fun iterator(): Iterator<T> = getAll().iterator()

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

var allProperties: Set<Property> = emptySet()

abstract class Property(val scope: Scope, val name: String) {
    init {
        allProperties += this
    }

    abstract val sqlType: String
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
}

open class IntProperty(scope: Scope, name: String) : PrimitiveProperty<Int>(scope, name) {
    override val sqlType = "INTEGER"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getIntProperty(oid, name)

    override fun setPrimitive(model: Model, oid: Oid, value: Int) =
            model.datastore.setIntProperty(oid, name, value)
}

open class LongProperty(scope: Scope, name: String) : PrimitiveProperty<Long>(scope, name) {
    override val sqlType = "INTEGER"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getLongProperty(oid, name)

    override fun setPrimitive(model: Model, oid: Oid, value: Long) =
            model.datastore.setLongProperty(oid, name, value)
}

open class RealProperty(scope: Scope, name: String) : PrimitiveProperty<Double>(scope, name) {
    override val sqlType = "REAL"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getRealProperty(oid, name)

    override fun setPrimitive(model: Model, oid: Oid, value: Double) =
            model.datastore.setRealProperty(oid, name, value)
}

open class StringProperty(scope: Scope, name: String) : PrimitiveProperty<String>(scope, name) {
    override val sqlType = "TEXT"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getStringProperty(oid, name)

    override fun setPrimitive(model: Model, oid: Oid, value: String) =
            model.datastore.setStringProperty(oid, name, value)
}

open class RefProperty<T : SThing>(scope: Scope, name: String, val kclass: KClass<T>) :
        PrimitiveProperty<T?>(scope, name) {
    override val sqlType = "INTEGER REFERENCES objects(oid) ON DELETE CASCADE"

    override fun getPrimitive(model: Model, oid: Oid) =
            model.datastore.getOidProperty(oid, name)?.load(model, kclass)

    override fun setPrimitive(model: Model, oid: Oid, value: T?) =
            model.datastore.setOidProperty(oid, name, value?.oid)
}

open class SetProperty<T : SThing>(scope: Scope, name: String, val kclass: KClass<T>)
    : Property(scope, name), ValProxy<Aggregate<T>> {
    override val sqlType = "INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE"

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

            override fun getAll(): List<T> =
                    set.getAll().map { it.load(model, kclass) }

            override fun getOne(): T? = set.getOne()?.load(model, kclass)
        }
    }
}

val ASTEROIDS_M = IntProperty(Scope.LOCAL, "asteroids_m")
val ASTEROIDS_O = IntProperty(Scope.LOCAL, "asteroids_o")
val BRIGHTNESS = RealProperty(Scope.GLOBAL, "brightness")
val CONTENTS = SetProperty(Scope.LOCAL, "contents", SThing::class)
val DATA = StringProperty(Scope.LOCAL, "data")
val FRAMES = SetProperty(Scope.LOCAL, "frames", SFrame::class)
val GALAXY = RefProperty(Scope.GLOBAL, "galaxy", SGalaxy::class)
val KIND = StringProperty(Scope.GLOBAL, "kind")
val LOCATION = RefProperty(Scope.LOCAL, "location", SThing::class)
val NAME = StringProperty(Scope.LOCAL, "name")
val OWNER = RefProperty(Scope.LOCAL, "owner", SPlayer::class)
val PASSWORD_HASH = StringProperty(Scope.SERVERONLY, "password_hash")
val PLAYERS = SetProperty(Scope.SERVERONLY, "players", SPlayer::class)
val SHIPS = SetProperty(Scope.GLOBAL, "ships", SShip::class)
val XPOS = RealProperty(Scope.LOCAL, "x")
val YPOS = RealProperty(Scope.LOCAL, "y")

