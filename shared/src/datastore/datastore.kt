package datastore

import shared.SThing
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

typealias Oid = Int

enum class Scope {
    SERVERONLY,
    PRIVATE,
    LOCAL,
    GLOBAL,
}

interface Aggregate<T>: Iterable<T> {
    fun add(item: T): Aggregate<T>
    fun remove(item: T): Aggregate<T>
    operator fun contains(item: T): Boolean
    fun getAll(): List<T>
    fun getOne(): T?

    fun forEach(action: (T) -> Unit) = getAll().forEach(action)
    operator fun plusAssign(item: T) { add(item) }
    operator fun minusAssign(item: T) { remove(item) }
    override fun iterator(): Iterator<T> = getAll().iterator()
}

interface Proxy<T> {
    fun get(): T
    fun set(value: T)

    operator fun getValue(thing: SThing, property: KProperty<*>): T = get()
    operator fun setValue(thing: SThing, property: KProperty<*>, value: T) = set(value)
}

interface Property<T> {
    val scope: Scope
    val name: String
}

interface PrimitiveProperty<T> : Property<T> {
    fun get(oid: Oid): Proxy<T>
}

interface AggregateProperty<T> : Property<T> {
    fun get(oid: Oid): Aggregate<T>
}

expect fun stringProperty(scope: Scope, name: String): PrimitiveProperty<String>
expect fun intProperty(scope: Scope, name: String): PrimitiveProperty<Int>
expect fun floatProperty(scope: Scope, name: String): PrimitiveProperty<Double>
expect fun <T : SThing> refProperty(scope: Scope, name: String, klass: KClass<T>): PrimitiveProperty<T?>
expect fun <T : SThing> setProperty(scope: Scope, name: String, klass: KClass<T>): AggregateProperty<T>

expect fun createObject(): Oid
expect fun doesObjectExist(oid: Oid): Boolean

