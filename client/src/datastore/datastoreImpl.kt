package datastore

actual fun stringProperty(scope: Scope, name: String): PrimitiveProperty<String> = TODO("unimplemented")
actual fun intProperty(scope: Scope, name: String): PrimitiveProperty<Int> = TODO("unimplemented")
actual fun floatProperty(scope: Scope, name: String): PrimitiveProperty<Double> = TODO("unimplemented")
actual fun <T> refProperty(scope: Scope, name: String): PrimitiveProperty<T> = TODO("unimplemented")
actual fun <T> setProperty(scope: Scope, name: String): AggregateProperty<T> = TODO("unimplemented")

actual fun createObject(kind: String): Oid = TODO("unimplemented")

actual fun openDatabase(filename: String) {
    TODO("unimplemented")
}

actual fun execute(sql: String) {
    TODO("unimplemented")
}



