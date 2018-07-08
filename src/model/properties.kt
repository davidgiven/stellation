package model

import interfaces.IDatastore
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

interface VarProxy<T> : ReadWriteProperty<SThing, T> {
    fun get(): T
    fun set(value: T)

    override operator fun getValue(thisRef: SThing, property: KProperty<*>): T = get()
    override operator fun setValue(thisRef: SThing, property: KProperty<*>, value: T) = set(value)
}

interface ValProxy<T> : ReadOnlyProperty<SThing, T> {
    fun get(): T

    override operator fun getValue(thisRef: SThing, property: KProperty<*>): T = get()
}

var allProperties: Set<Property> = emptySet()

abstract class Property(val scope: Scope, val name: String) {
    init {
        allProperties += this
    }

    abstract val sqlType: String

    protected val datastore: IDatastore get() = utils.get()
}

abstract class PrimitiveProperty<T>(scope: Scope, name: String) : Property(scope, name) {
    protected open fun getPrimitive(oid: Oid): T = TODO("get get $name yet")
    protected open fun setPrimitive(oid: Oid, value: T): Unit = TODO("can't set $name yet")

    fun get(oid: Oid): VarProxy<T> =
            object : VarProxy<T> {
                override fun get(): T = getPrimitive(oid)
                override fun set(value: T) = setPrimitive(oid, value)
            }
}

open class IntProperty(scope: Scope, name: String) : PrimitiveProperty<Int>(scope, name) {
    override val sqlType = "INTEGER"

    override fun getPrimitive(oid: Oid) =
            datastore.getIntProperty(oid, name)

    override fun setPrimitive(oid: Oid, value: Int) =
            datastore.setIntProperty(oid, name, value)
}

open class LongProperty(scope: Scope, name: String) : PrimitiveProperty<Long>(scope, name) {
    override val sqlType = "INTEGER"

    override fun getPrimitive(oid: Oid) =
            datastore.getLongProperty(oid, name)

    override fun setPrimitive(oid: Oid, value: Long) =
            datastore.setLongProperty(oid, name, value)
}

open class RealProperty(scope: Scope, name: String) : PrimitiveProperty<Double>(scope, name) {
    override val sqlType = "REAL"

    override fun getPrimitive(oid: Oid) =
            datastore.getRealProperty(oid, name)

    override fun setPrimitive(oid: Oid, value: Double) =
            datastore.setRealProperty(oid, name, value)
}

open class StringProperty(scope: Scope, name: String) : PrimitiveProperty<String>(scope, name) {
    override val sqlType = "TEXT"

    override fun getPrimitive(oid: Oid) =
            datastore.getStringProperty(oid, name)

    override fun setPrimitive(oid: Oid, value: String) =
            datastore.setStringProperty(oid, name, value)
}

open class RefProperty<T : SThing>(scope: Scope, name: String, val kclass: KClass<T>) :
        PrimitiveProperty<T?>(scope, name) {
    override val sqlType = "INTEGER REFERENCES objects(oid) ON DELETE CASCADE"

    override fun getPrimitive(oid: Oid) =
            datastore.getOidProperty(oid, name)?.load(kclass)

    override fun setPrimitive(oid: Oid, value: T?) =
            datastore.setOidProperty(oid, name, value?.oid)
}

open class SetProperty<T : SThing>(scope: Scope, name: String, val kclass: KClass<T>) : Property(scope, name) {
    override val sqlType = "INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE"

    fun get(oid: Oid): ValProxy<Aggregate<T>> =
            object : ValProxy<Aggregate<T>> {
                override fun get(): Aggregate<T> =
                        object : Aggregate<T> {
                            // We do this here to ensure that the context is dereferenced every
                            // time the aggregate is fetched.
                            val set = datastore.getSetProperty(oid, name)

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
                                    set.getAll().map { it.load(kclass) }

                            override fun getOne(): T? = set.getOne()?.load(kclass)
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

