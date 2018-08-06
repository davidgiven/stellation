package interfaces

import utils.Oid

interface SetProperty {
    fun add(item: Oid): SetProperty
    fun remove(item: Oid): SetProperty
    fun clear(): SetProperty
    operator fun contains(item: Oid): Boolean
    fun getAll(): List<Oid>
    fun getOne(): Oid?

    fun addAll(new: Iterable<Oid>) {
        for (oid in new) {
            add(oid)
        }
    }

    fun replaceAll(new: Iterable<Oid>) {
        clear()
        addAll(new)
    }
}

interface IDatastore {
    fun initialiseDatabase()

    fun createProperty(name: String, sqlType: String, isAggregate: Boolean)

    fun createObject(): Oid
    fun createObject(oid: Oid)
    fun destroyObject(oid: Oid)
    fun getAllObjects(): List<Oid>
    fun doesObjectExist(oid: Oid): Boolean

    fun hasProperty(oid: Oid, name: String): Boolean

    fun setOidProperty(oid: Oid, name: String, value: Oid?)
    fun getOidProperty(oid: Oid, name: String): Oid?

    fun setIntProperty(oid: Oid, name: String, value: Int)
    fun getIntProperty(oid: Oid, name: String): Int

    fun setLongProperty(oid: Oid, name: String, value: Long)
    fun getLongProperty(oid: Oid, name: String): Long

    fun setRealProperty(oid: Oid, name: String, value: Double)
    fun getRealProperty(oid: Oid, name: String): Double

    fun setStringProperty(oid: Oid, name: String, value: String)
    fun getStringProperty(oid: Oid, name: String): String

    fun getSetProperty(oid: Oid, name: String): SetProperty

    fun createSyncSession(): Int
    fun getPropertiesChangedSince(oids: List<Oid>, session: Int): List<Pair<Oid, String>>
    fun propertySeenBy(oid: Oid, name: String, session: Int)

    fun getHierarchy(root: Oid, containment: String): Set<Oid>
}
