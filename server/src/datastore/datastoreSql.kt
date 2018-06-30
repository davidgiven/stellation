package datastore

import shared.SThing
import shared.initProperties
import kotlin.reflect.KProperty

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

    open fun getPrimitive(oid: Oid): T = TODO("can't get $name")
    open fun setPrimitive(oid: Oid, value: T): Unit = TODO("can't set $name")

    override fun get(thing: SThing): Proxy<T> =
            object : Proxy<T> {
                override fun setValue(thing: SThing, property: KProperty<*>, value: T) =
                        setPrimitive(thing.oid, value)

                override fun getValue(thing: SThing, property: KProperty<*>): T =
                        getPrimitive(thing.oid)
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

    override fun get(thing: SThing): Aggregate<T> {
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

            override fun getPrimitive(oid: Oid): String =
                    sqlStatement("SELECT value FROM prop_$name WHERE oid = ?")
                            .bindInt(1, oid)
                            .executeSimpleQuery()
                            .getValue("value")
                            .getString()

            override fun setPrimitive(oid: Oid, value: String) {
                sqlStatement("INSERT OR REPLACE INTO prop_$name (oid, value) VALUES (?, ?)")
                        .bindInt(1, oid)
                        .bindString(2, value)
                        .executeStatement()
            }
        })

actual fun intProperty(scope: Scope, name: String): PrimitiveProperty<Int> =
        addProperty(object : PrimitivePropertyImpl<Int>(scope, name) {
            override val sqlType = "INTEGER"

            override fun getPrimitive(oid: Oid): Int =
                    sqlStatement("SELECT value FROM prop_$name WHERE oid = ?")
                            .bindInt(1, oid)
                            .executeSimpleQuery()
                            .getValue("value")
                            .getInt()

            override fun setPrimitive(oid: Oid, value: Int) {
                sqlStatement("INSERT OR REPLACE INTO prop_$name (oid, value) VALUES (?, ?)")
                        .bindInt(1, oid)
                        .bindInt(2, value)
                        .executeStatement()
            }
        })

actual fun floatProperty(scope: Scope, name: String): PrimitiveProperty<Double> =
        addProperty(object : PrimitivePropertyImpl<Double>(scope, name) {
            override val sqlType = "REAL"

            override fun getPrimitive(oid: Oid): Double =
                    sqlStatement("SELECT value FROM prop_$name WHERE oid = ?")
                            .bindInt(1, oid)
                            .executeSimpleQuery()
                            .getValue("value")
                            .getReal()

            override fun setPrimitive(oid: Oid, value: Double) {
                sqlStatement("INSERT OR REPLACE INTO prop_$name (oid, value) VALUES (?, ?)")
                        .bindInt(1, oid)
                        .bindReal(2, value)
                        .executeStatement()
            }
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
    sqlStatement("INSERT INTO prop_kind (oid, value) VALUES(NULL, ?)")
            .bindString(1, kind)
            .executeStatement()
    return sqlStatement("SELECT last_insert_rowid() AS oid")
            .executeSimpleQuery()
            .getValue("oid")
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
    withSqlTransaction {
        allProperties.forEach { e -> e.value.createTablesForProperty() }
    }
}
