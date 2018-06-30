package datastore

import shared.SThing
import utils.UNIMPLEMENTED

actual fun stringProperty(scope: Scope, name: String): PrimitiveProperty<String> = TODO("unimplemented")
actual fun intProperty(scope: Scope, name: String): PrimitiveProperty<Int> = TODO("unimplemented")
actual fun floatProperty(scope: Scope, name: String): PrimitiveProperty<Double> = TODO("unimplemented")
actual fun <T: SThing> refProperty(scope: Scope, name: String, constructor: () -> T): PrimitiveProperty<T?> = UNIMPLEMENTED()
actual fun <T> setProperty(scope: Scope, name: String): AggregateProperty<T> = TODO("unimplemented")

actual fun createObject(): Oid = UNIMPLEMENTED()
actual fun doesObjectExist(oid: Oid): Boolean = UNIMPLEMENTED()

