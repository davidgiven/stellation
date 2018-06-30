package datastore

import shared.SThing
import kotlin.reflect.KProperty

typealias Oid = Int

enum class Scope {
    SERVERONLY,
    PRIVATE,
    LOCAL,
    GLOBAL,
}

interface Aggregate<T> {
    fun add(item: T)
    fun remove(item: T)
    fun contains(item: T): Boolean
    fun getAll(): Array<T>
    fun getOne(): T
    fun forEach(action: (T) -> Unit)
}

interface Proxy<T> {
    operator fun getValue(thing: SThing, property: KProperty<*>): T
    operator fun setValue(thing: SThing, property: KProperty<*>, value: T)
}

interface Property<T> {
    val scope: Scope
    val name: String
}

interface PrimitiveProperty<T>: Property<T> {
    fun get(oid: Oid): Proxy<T>
}

interface AggregateProperty<T>: Property<T> {
    fun get(oid: Oid): Aggregate<T>
}

expect fun stringProperty(scope: Scope, name: String): PrimitiveProperty<String>
expect fun intProperty(scope: Scope, name: String): PrimitiveProperty<Int>
expect fun floatProperty(scope: Scope, name: String): PrimitiveProperty<Double>
expect fun <T> refProperty(scope: Scope, name: String): PrimitiveProperty<T>
expect fun <T> setProperty(scope: Scope, name: String): AggregateProperty<T>

expect fun createObject(kind: String): Oid

