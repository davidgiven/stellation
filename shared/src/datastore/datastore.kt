package datastore

import model.SThing
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

typealias Oid = Int

enum class Scope {
    SERVERONLY,
    PRIVATE,
    LOCAL,
    GLOBAL,
}

interface Aggregate<T> : Iterable<T> {
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

interface Property<T> {
    val scope: Scope
    val name: String
}

interface PrimitiveProperty<T> : Property<T> {
    fun get(oid: Oid): VarProxy<T>
}

interface AggregateProperty<T> : Property<T> {
    fun get(oid: Oid): ValProxy<Aggregate<T>>
}

expect fun stringProperty(scope: Scope, name: String): PrimitiveProperty<String>
expect fun intProperty(scope: Scope, name: String): PrimitiveProperty<Int>
expect fun floatProperty(scope: Scope, name: String): PrimitiveProperty<Double>
expect fun <T : SThing> refProperty(scope: Scope, name: String, klass: KClass<T>): PrimitiveProperty<T?>
expect fun <T : SThing> setProperty(scope: Scope, name: String, klass: KClass<T>): AggregateProperty<T>

expect fun createObject(): Oid
expect fun destroyObject(oid: Oid)
expect fun doesObjectExist(oid: Oid): Boolean

