package datastore

import model.SThing
import utils.UNIMPLEMENTED
import kotlin.reflect.KClass

actual fun stringProperty(scope: Scope, name: String): PrimitiveProperty<String> = TODO("unimplemented")
actual fun intProperty(scope: Scope, name: String): PrimitiveProperty<Int> = TODO("unimplemented")
actual fun floatProperty(scope: Scope, name: String): PrimitiveProperty<Double> = TODO("unimplemented")
actual fun <T : SThing> refProperty(scope: Scope, name: String, klass: KClass<T>): PrimitiveProperty<T?> =
        UNIMPLEMENTED()

actual fun <T : SThing> setProperty(scope: Scope, name: String, klass: KClass<T>): AggregateProperty<T> =
        UNIMPLEMENTED()

actual fun createObject(): Oid = UNIMPLEMENTED()
actual fun destroyObject(oid: Oid): Unit = UNIMPLEMENTED()
actual fun doesObjectExist(oid: Oid): Boolean = UNIMPLEMENTED()

