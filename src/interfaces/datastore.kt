package interfaces

import datastore.Oid

interface SetProperty {
    fun add(item: Oid): SetProperty
    fun remove(item: Oid): SetProperty
    fun clear(): SetProperty
    operator fun contains(item: Oid): Boolean
    fun getAll(): List<Oid>
}

interface IDatastore {
    fun initialiseDatabase()

    fun createObject(): Oid
    fun destroyObject(oid: Oid)
    fun doesObjectExist(oid: Oid): Boolean

    fun setIntProperty(oid: Oid, name: String, value: Int)
    fun getIntProperty(oid: Oid, name: String): Int

    fun setLongProperty(oid: Oid, name: String, value: Long)
    fun getLongProperty(oid: Oid, name: String): Long

    fun setRealProperty(oid: Oid, name: String, value: Double)
    fun getRealProperty(oid: Oid, name: String): Double

    fun setStringProperty(oid: Oid, name: String, value: String)
    fun getStringProperty(oid: Oid, name: String): String

    fun getSetProperty(oid: Oid, name: String): SetProperty
}
