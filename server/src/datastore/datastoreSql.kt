package datastore

import shared.SUniverse
import shared.create
import shared.initProperties

private var allProperties: Map<String, PropertyImpl<*>> = emptyMap()

private abstract class PropertyImpl<T>(override val scope: Scope, override val name: String) : Property<T> {
    abstract val sqlType: String

    abstract fun createTablesForProperty()
}

private abstract class PrimitivePropertyImpl<T>(scope: Scope, name: String) : PropertyImpl<T>(scope, name), PrimitiveProperty<T> {
    override fun createTablesForProperty() {
        executeSql("""
            CREATE TABLE IF NOT EXISTS prop_$name (
                oid INTEGER PRIMARY KEY REFERENCES prop_kind(oid) ON DELETE CASCADE,
                value $sqlType
            )
        """)
    }

    override fun get(oid: Oid): Proxy<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

private abstract class AggregatePropertyImpl<T>(scope: Scope, name: String) : PropertyImpl<T>(scope, name), AggregateProperty<T> {
    override fun createTablesForProperty() {
        executeSql("""
            CREATE TABLE IF NOT EXISTS prop_$name (
                oid INTEGER NOT NULL REFERENCES prop_kind(oid) ON DELETE CASCADE,
                value $sqlType
            )
        """)
        executeSql("""
            CREATE INDEX IF NOT EXISTS index_byoid_$name ON prop_$name (oid)
        """)
        executeSql("""
            CREATE INDEX IF NOT EXISTS index_byboth_$name ON prop_$name (oid, value)
        """)
    }

    override fun get(oid: Oid): Aggregate<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

private fun <T : PropertyImpl<*>> addProperty(property: T): T {
    check(!allProperties.contains(property.name))
    allProperties += Pair(property.name, property)
    return property
}

actual fun stringProperty(scope: Scope, name: String): PrimitiveProperty<String> =
        addProperty(object : PrimitivePropertyImpl<String>(scope, name) {
            override val sqlType = "TEXT"
        })

actual fun intProperty(scope: Scope, name: String): PrimitiveProperty<Int> =
        addProperty(object : PrimitivePropertyImpl<Int>(scope, name) {
            override val sqlType = "INTEGER"
        })

actual fun floatProperty(scope: Scope, name: String): PrimitiveProperty<Double> =
        addProperty(object : PrimitivePropertyImpl<Double>(scope, name) {
            override val sqlType = "REAL"
        })

actual fun <T> refProperty(scope: Scope, name: String): PrimitiveProperty<T> =
        addProperty(object : PrimitivePropertyImpl<T>(scope, name) {
            override val sqlType = "INTEGER REFERENCES prop_kind(oid) ON DELETE CASCADE"
        })

actual fun <T> setProperty(scope: Scope, name: String): AggregateProperty<T> =
        addProperty(object : AggregatePropertyImpl<T>(scope, name) {
            override val sqlType = "INTEGER NOT NULL REFERENCES prop_kind(oid) ON DELETE CASCADE"
        })

actual fun createObject(kind: String): Oid {
    sqlStatement("INSERT INTO prop_kind (oid, kind) VALUES(:oid, :kind)")
            .bindInt(":oid", null)
            .bindString(":kind", kind)
            .execute()
    return sqlStatement("SELECT last_insert_rowid() AS oid")
            .execute()
            .first()["oid"]!!
            .getInt()
}

fun withSqlTransaction(callback: () -> Unit) {
    executeSql("BEGIN")
    try {
        callback()
        executeSql("COMMIT")
    } catch (t: Throwable) {
        executeSql("ROLLBACK")
        throw t
    }
}

fun initialiseDatabase() {
    initProperties()
    executeSql("PRAGMA auto_vacuum = FULL")
    executeSql("PRAGMA encoding = \"UTF-8\"")
    executeSql("PRAGMA synchronous = OFF")
    executeSql("PRAGMA foreign_keys = ON")
    executeSql("PRAGMA temp_store = MEMORY")
    withSqlTransaction {
        allProperties.forEach { e -> e.value.createTablesForProperty() }
    }

    val universe = SUniverse().create()
}
