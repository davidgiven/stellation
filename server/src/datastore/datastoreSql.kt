package datastore

import shared.SThing
import shared.initClasses
import shared.initProperties
import shared.load
import kotlin.reflect.KClass

private var allProperties: Map<String, PropertyImpl<*>> = emptyMap()

private abstract class PropertyImpl<T>(override val scope: Scope, override val name: String) : Property<T> {
    abstract val sqlType: String

    abstract fun createTablesForProperty()
}

private abstract class PrimitivePropertyImpl<T>(scope: Scope, name: String) : PropertyImpl<T>(scope, name),
        PrimitiveProperty<T> {
    override fun createTablesForProperty() {
        executeSql(
                """
            CREATE TABLE IF NOT EXISTS prop_$name (
                oid INTEGER PRIMARY KEY REFERENCES objects(oid) ON DELETE CASCADE,
                value $sqlType
            )
        """)
    }

    abstract val defaultValue: T

    open fun getPrimitive(oid: Oid): T? = TODO("can't get $name")
    open fun setPrimitive(oid: Oid, value: T): Unit = TODO("can't set $name")

    override fun get(oid: Oid): Proxy<T> =
            object : Proxy<T> {
                override fun set(value: T) = setPrimitive(oid, value)
                override fun get(): T = getPrimitive(oid) ?: defaultValue
            }
}

private abstract class AggregatePropertyImpl<T : SThing>(scope: Scope, name: String) :
        PropertyImpl<T>(scope, name), AggregateProperty<T> {
    override val sqlType = "INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE"

    override fun createTablesForProperty() {
        executeSql(
                """
            CREATE TABLE IF NOT EXISTS prop_$name (
                oid INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                value $sqlType
            )
        """)
        executeSql(
                """
            CREATE INDEX IF NOT EXISTS index_byoid_$name ON prop_$name (oid)
        """)
        executeSql(
                """
            CREATE INDEX IF NOT EXISTS index_byboth_$name ON prop_$name (oid, value)
        """)
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
            override val defaultValue = ""

            override fun getPrimitive(oid: Oid): String? =
                    sqlStatement("SELECT value FROM prop_$name WHERE oid = ?")
                            .bindInt(1, oid)
                            .executeSimpleQuery()
                            ?.get("value")
                            ?.getString()

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
            override val defaultValue = 0

            override fun getPrimitive(oid: Oid): Int? =
                    sqlStatement("SELECT value FROM prop_$name WHERE oid = ?")
                            .bindInt(1, oid)
                            .executeSimpleQuery()
                            ?.get("value")
                            ?.getInt()

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
            override val defaultValue = 0.0

            override fun getPrimitive(oid: Oid): Double? =
                    sqlStatement("SELECT value FROM prop_$name WHERE oid = ?")
                            .bindInt(1, oid)
                            .executeSimpleQuery()
                            ?.get("value")
                            ?.getReal()

            override fun setPrimitive(oid: Oid, value: Double) {
                sqlStatement("INSERT OR REPLACE INTO prop_$name (oid, value) VALUES (?, ?)")
                        .bindInt(1, oid)
                        .bindReal(2, value)
                        .executeStatement()
            }
        })

actual fun <T : SThing> refProperty(scope: Scope, name: String, klass: KClass<T>): PrimitiveProperty<T?> =
        addProperty(object : PrimitivePropertyImpl<T?>(scope, name) {
            override val sqlType = "INTEGER REFERENCES objects(oid) ON DELETE CASCADE"
            override val defaultValue = null

            override fun getPrimitive(oid: Oid): T? =
                    sqlStatement("SELECT value FROM prop_$name WHERE oid = ?")
                            .bindInt(1, oid)
                            .executeSimpleQuery()
                            ?.get("value")
                            ?.getOid()
                            ?.load(klass)

            override fun setPrimitive(oid: Oid, value: T?) =
                    sqlStatement("INSERT OR REPLACE INTO prop_$name (oid, value) VALUES (?, ?)")
                            .bindInt(1, oid)
                            .bindOid(2, value?.oid)
                            .executeStatement()
        })

actual fun <T : SThing> setProperty(scope: Scope, name: String, klass: KClass<T>): AggregateProperty<T> =
        addProperty(object : AggregatePropertyImpl<T>(scope, name) {
            override fun get(oid: Oid): Aggregate<T> =
                    object : Aggregate<T> {
                        override fun add(item: T): Aggregate<T> {
                            sqlStatement("INSERT OR IGNORE INTO prop_$name (oid, value) VALUES (?, ?)")
                                    .bindInt(1, oid)
                                    .bindOid(2, item.oid)
                                    .executeStatement()
                            return this
                        }

                        override fun remove(item: T): Aggregate<T> {
                            sqlStatement("DELETE FROM prop_$name WHERE oid = ? AND value = ?")
                                    .bindInt(1, oid)
                                    .bindOid(2, item.oid)
                                    .executeStatement()
                            return this
                        }

                        override fun clear(): Aggregate<T> {
                            sqlStatement("DELETE FROM prop_$name WHERE oid = ?")
                                    .bindInt(1, oid)
                                    .executeStatement()
                            return this
                        }

                        override fun contains(item: T): Boolean =
                                sqlStatement(
                                        "SELECT COUNT(*) AS count FROM prop_$name WHERE oid = ? AND value = ? LIMIT 1")
                                        .bindInt(1, oid)
                                        .bindOid(2, item.oid)
                                        .executeSimpleQuery()!!
                                        .getValue("count")
                                        .getInt() != 0

                        override fun getAll(): List<T> =
                                sqlStatement("SELECT value FROM prop_$name WHERE oid = ?")
                                        .bindInt(1, oid)
                                        .executeQuery()
                                        .map { it.getValue("value").getOid()!!.load(klass) }

                        override fun getOne(): T? =
                                sqlStatement("SELECT value FROM prop_$name WHERE oid = ? LIMIT 1")
                                        .bindInt(1, oid)
                                        .executeSimpleQuery()
                                        ?.getValue("value")
                                        ?.getOid()
                                        ?.load(klass)
                    }
        })

actual fun createObject(): Oid {
    sqlStatement("INSERT INTO objects (oid) VALUES(NULL)")
            .executeStatement()
    return sqlStatement("SELECT last_insert_rowid() AS oid")
            .executeSimpleQuery()!!
            .getValue("oid")
            .getOid()!!
}

actual fun destroyObject(oid: Oid) {
    sqlStatement("DELETE FROM objects WHERE oid = ?")
            .bindInt(1, oid)
            .executeStatement()
}

actual fun doesObjectExist(oid: Oid) =
        sqlStatement("SELECT COUNT(*) AS count FROM objects WHERE oid = ? LIMIT 1")
                .bindOid(1, oid)
                .executeSimpleQuery()!!
                .getValue("count")
                .getInt() != 0

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
    initClasses()
    withSqlTransaction {
        executeSql(
                """
            CREATE TABLE IF NOT EXISTS objects (
                oid INTEGER PRIMARY KEY AUTOINCREMENT
            )
        """)
        allProperties.forEach { e -> e.value.createTablesForProperty() }
    }
}
