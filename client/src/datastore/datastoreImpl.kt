package datastore

import model.SThing
import model.load
import utils.UNIMPLEMENTED
import kotlin.reflect.KClass

private var propertyCache: Map<Pair<Oid, String>, dynamic> = emptyMap()

private abstract class PropertyImpl<T>(override val scope: Scope, override val name: String) : Property<T>

private abstract class PrimitivePropertyImpl<T>(scope: Scope, name: String) : PropertyImpl<T>(scope, name),
        PrimitiveProperty<T> {
    abstract val defaultValue: T

    override fun get(oid: Oid): VarProxy<T> =
            object : VarProxy<T> {
                override fun set(value: T) = UNIMPLEMENTED()
                override fun get(): T =
                        propertyCache.get(Pair(oid, name))?.unsafeCast<T>() ?: defaultValue
            }
}

private class RefPropertyImpl<T : SThing>(scope: Scope, name: String, val kclass: KClass<T>) :
        PropertyImpl<T?>(scope, name),
        PrimitiveProperty<T?> {
    override fun get(oid: Oid): VarProxy<T?> =
            object : VarProxy<T?> {
                override fun set(value: T?) = UNIMPLEMENTED()
                override fun get(): T? =
                        propertyCache.get(Pair(oid, name))
                                ?.unsafeCast<Oid>()
                                ?.load(kclass)
            }
}

private class SetPropertyImpl<T : SThing>(scope: Scope, name: String, val kclass: KClass<T>) :
        PropertyImpl<T>(scope, name),
        AggregateProperty<T> {
    override fun get(oid: Oid): ValProxy<Aggregate<T>> =
            object : ValProxy<Aggregate<T>>, Aggregate<T> {
                override fun get(): Aggregate<T> = this

                override fun remove(item: T): Aggregate<T> = UNIMPLEMENTED()
                override fun clear(): Aggregate<T> = UNIMPLEMENTED()
                override fun add(item: T): Aggregate<T> = UNIMPLEMENTED()

                private fun getSet() = propertyCache.get(Pair(oid, name))
                        ?.unsafeCast<Set<Oid>>()
                        ?: emptySet()

                override fun contains(item: T): Boolean =
                        getSet().contains(item.oid)

                override fun getAll(): List<T> =
                        getSet().map { it -> it.load(kclass) }

                override fun getOne(): T? =
                        getSet().firstOrNull()?.load(kclass)
            }
}

actual fun stringProperty(scope: Scope, name: String): PrimitiveProperty<String> =
        object : PrimitivePropertyImpl<String>(scope, name) {
            override val defaultValue = ""
        }

actual fun intProperty(scope: Scope, name: String): PrimitiveProperty<Int> =
        object : PrimitivePropertyImpl<Int>(scope, name) {
            override val defaultValue = 0
        }

actual fun longProperty(scope: Scope, name: String): PrimitiveProperty<Long> =
        object : PrimitivePropertyImpl<Long>(scope, name) {
            override val defaultValue = 0L
        }

actual fun floatProperty(scope: Scope, name: String): PrimitiveProperty<Double> =
        object : PrimitivePropertyImpl<Double>(scope, name) {
            override val defaultValue = 0.0
        }

actual fun <T : SThing> refProperty(scope: Scope, name: String, klass: KClass<T>): PrimitiveProperty<T?> =
        RefPropertyImpl(scope, name, klass)

actual fun <T : SThing> setProperty(scope: Scope, name: String, klass: KClass<T>): AggregateProperty<T> =
        SetPropertyImpl(scope, name, klass)

actual fun createObject(): Oid = UNIMPLEMENTED()
actual fun destroyObject(oid: Oid): Unit = UNIMPLEMENTED()
actual fun doesObjectExist(oid: Oid): Boolean = UNIMPLEMENTED()

fun setRawProperty(oid: Oid, name: String, value: dynamic) {
    propertyCache += Pair(Pair(oid, name), value)
}

