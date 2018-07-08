package interfaces

typealias Oid = Int

interface SetProperty {
    fun add(item: Oid): SetProperty
    fun remove(item: Oid): SetProperty
    fun clear(): SetProperty
    operator fun contains(item: Oid): Boolean
    fun getAll(): List<Oid>
    fun getOne(): Oid?
}

interface IDatastore {
    fun initialiseDatabase()

    fun createProperty(name: String, sqlType: String)

    fun createObject(): Oid
    fun destroyObject(oid: Oid)
    fun doesObjectExist(oid: Oid): Boolean

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
}
