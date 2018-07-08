package interfaces

import kotlin.reflect.KClass

interface IObject {
    val oid: Oid
}

interface IModel {
    fun <T : IObject> loadRawObject(oid: Oid, constructor: (IModel, Oid) -> T): T
    fun <T : IObject> loadObject(oid: Oid, constructor: (IModel, Oid) -> T): T

    fun <T : IObject> create(kclass: KClass<T>): T
}
