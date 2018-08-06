package runtime.shared

import interfaces.IDatastore
import utils.Oid
import interfaces.SetProperty
import interfaces.UNIMPLEMENTED
import utils.Fault
import utils.FaultDomain.INVALID_ARGUMENT

fun throwBadDatastoreTypeException(): Nothing =
        throw Fault(INVALID_ARGUMENT).withDetail("type mismatch on datastore access")


private abstract class Value {
    open fun getOid(): Oid? = throwBadDatastoreTypeException()
    open fun getInt(): Int = throwBadDatastoreTypeException()
    open fun getLong(): Long = throwBadDatastoreTypeException()
    open fun getReal(): Double = throwBadDatastoreTypeException()
    open fun getString(): String = throwBadDatastoreTypeException()
    open fun getSet(): SetProperty = throwBadDatastoreTypeException()
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
    private var maxOid: Int = 1
    private lateinit var objects: Set<Oid>
    private lateinit var values: Map<Pair<Oid, String>, Value>
    private lateinit var properties: Set<String>

    override fun initialiseDatabase() {
        maxOid = 1
        objects = emptySet()
        values = emptyMap()
        properties = emptySet()
    }

    override fun createProperty(name: String, sqlType: String, isAggregate: Boolean) {
        properties += name
    }

    override fun createObject(): Oid {
        val newoid = maxOid++
        objects += newoid
        return newoid
    }

    override fun createObject(oid: Oid) {
        if (oid > maxOid) {
            maxOid = oid + 1
        }
        objects += oid
    }

    override fun destroyObject(oid: Oid) {
        objects -= oid
    }

    override fun getAllObjects(): List<Oid> = objects.toList()

    override fun doesObjectExist(oid: Oid): Boolean = objects.contains(oid)

    override fun hasProperty(oid: Oid, name: String) = Pair(oid, name) in values

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

    override fun createSyncSession() = UNIMPLEMENTED()
    override fun getPropertiesChangedSince(oids: List<Oid>, session: Int) = UNIMPLEMENTED()
    override fun propertySeenBy(oid: Oid, name: String, session: Int) = UNIMPLEMENTED()

    override fun getHierarchy(root: Oid, containment: String): Set<Oid> {
        var set = emptySet<Oid>()
        set += root
        for (child in getSetProperty(root, containment).getAll()) {
            set += getHierarchy(child, containment)
        }
        return set
    }
}