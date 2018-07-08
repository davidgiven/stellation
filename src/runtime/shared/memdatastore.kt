package runtime.shared

import datastore.Oid
import interfaces.IDatastore
import interfaces.SetProperty

class BadDatastoreTypeException : RuntimeException("type mismatch on datastore access")

private abstract class Value {
    open fun getOid(): Oid? = throw BadDatastoreTypeException()
    open fun getInt(): Int = throw BadDatastoreTypeException()
    open fun getLong(): Long = throw BadDatastoreTypeException()
    open fun getReal(): Double = throw BadDatastoreTypeException()
    open fun getString(): String = throw BadDatastoreTypeException()
    open fun getSet(): SetProperty = throw BadDatastoreTypeException()
}

private class OidValue(val value: Oid?) : Value() {
    override fun getOid() = value
}

private class IntValue(val value: Int) : Value() {
    override fun getInt() = value
}

private class LongValue(val value: Long) : Value() {
    override fun getLong() = value
}

private class RealValue(val value: Double) : Value() {
    override fun getReal() = value
}

private class StringValue(val value: String) : Value() {
    override fun getString() = value
}

private class SetValue(val value: SetProperty) : Value() {
    override fun getSet() = value
}

class InMemoryDatastore : IDatastore {
    private var oid: Int = 1
    private lateinit var objects: Set<Oid>
    private lateinit var values: Map<Pair<Oid, String>, Value>
    private lateinit var properties: Set<String>

    override fun initialiseDatabase() {
        oid = 1
        objects = emptySet()
        values = emptyMap()
        properties = emptySet()
    }

    override fun createProperty(name: String, sqlType: String) {
        properties += name
    }

    override fun createObject(): Oid {
        val newoid = oid++
        objects += newoid
        return newoid
    }

    override fun destroyObject(oid: Oid) {
        objects -= oid
        properties.forEach { values -= Pair(oid, it) }
    }

    override fun doesObjectExist(oid: Oid): Boolean = objects.contains(oid)

    override fun setOidProperty(oid: Oid, name: String, value: Oid?) {
        values += Pair(oid, name) to OidValue(value)
    }

    override fun getOidProperty(oid: Oid, name: String): Oid? =
            values[Pair(oid, name)]?.getOid()

    override fun setIntProperty(oid: Oid, name: String, value: Int) {
        values += Pair(oid, name) to IntValue(value)
    }

    override fun getIntProperty(oid: Oid, name: String): Int =
            values[Pair(oid, name)]?.getInt() ?: 0

    override fun setLongProperty(oid: Oid, name: String, value: Long) {
        values += Pair(oid, name) to LongValue(value)
    }

    override fun getLongProperty(oid: Oid, name: String): Long =
            values[Pair(oid, name)]?.getLong() ?: 0

    override fun setRealProperty(oid: Oid, name: String, value: Double) {
        values += Pair(oid, name) to RealValue(value)
    }

    override fun getRealProperty(oid: Oid, name: String): Double =
            values[Pair(oid, name)]?.getReal() ?: 0.0

    override fun setStringProperty(oid: Oid, name: String, value: String) {
        values += Pair(oid, name) to StringValue(value)
    }

    override fun getStringProperty(oid: Oid, name: String): String =
            values[Pair(oid, name)]?.getString() ?: ""

    override fun getSetProperty(oid: Oid, name: String): SetProperty {
        val key = Pair(oid, name)
        var set = values[key]?.getSet()
        if (set == null) {
            set = object : SetProperty {
                private var value: Set<Oid> = emptySet()

                override fun add(item: Oid): SetProperty {
                    value += item
                    return this
                }

                override fun remove(item: Oid): SetProperty {
                    value -= item
                    return this
                }

                override fun clear(): SetProperty {
                    value = emptySet()
                    return this
                }

                override fun contains(item: Oid): Boolean =
                        value.contains(item) && objects.contains(item)

                override fun getAll(): List<Oid> =
                        value.filter { objects.contains(it) }

                override fun getOne(): Oid? =
                        value.find { objects.contains(it) }
            }
            values += key to SetValue(set)
        }
        return set
    }
}