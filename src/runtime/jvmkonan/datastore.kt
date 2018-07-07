package runtime.jvmkonan

import datastore.IDatabase
import datastore.Oid
import datastore.withSqlTransaction
import interfaces.IDatastore
import interfaces.SetProperty

class SqlDatastore(val database: IDatabase) : IDatastore {
    override fun initialiseDatabase() {
        database.withSqlTransaction {
            database.executeSql(
                    """
                CREATE TABLE IF NOT EXISTS objects (
                    oid INTEGER PRIMARY KEY AUTOINCREMENT
            )
            """)
            database.executeSql(
                    """
                CREATE TABLE IF NOT EXISTS timers (
                    oid INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                    expiry INTEGER
            )
            """)
            database.executeSql(
                    """
                CREATE INDEX IF NOT EXISTS timers_by_expiry ON timers (expiry ASC)
            """)
//        allProperties.forEach { e -> e.value.createTablesForProperty() }
        }

    }

    override fun createProperty(name: String, sqlType: String) {
        database.executeSql(
                """
             CREATE TABLE IF NOT EXISTS prop_$name (
                oid INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                value $sqlType)
        """)
    }

    override fun createObject(): Oid {
        database.sqlStatement("INSERT INTO objects (oid) VALUES(NULL)")
                .executeStatement()
        return database.sqlStatement("SELECT last_insert_rowid() AS oid")
                .executeSimpleQuery()!!
                .getValue("oid")
                .getOid()!!

    }

    override fun destroyObject(oid: Oid) {
        database.sqlStatement("DELETE FROM objects WHERE oid = ?")
                .bindInt(1, oid)
                .executeStatement()
    }

    override fun doesObjectExist(oid: Oid): Boolean =
            database.sqlStatement("SELECT COUNT(*) AS count FROM objects WHERE oid = ? LIMIT 1")
                    .bindOid(1, oid)
                    .executeSimpleQuery()!!
                    .getValue("count")
                    .getInt() != 0

    private fun setStatement(oid: Oid, name: String) =
            database.sqlStatement(
                    """
                        INSERT OR REPLACE INTO prop_$name (oid, value) VALUES (?, ?)
                    """)
                    .bindOid(1, oid)

    private fun getStatement(oid: Oid, name: String) =
            database.sqlStatement(
                    """
                        SELECT value FROM prop_$name WHERE oid = ?
                    """)
                    .bindOid(1, oid)

    override fun setIntProperty(oid: Oid, name: String, value: Int): Unit =
            setStatement(oid, name).bindInt(2, value).executeStatement()

    override fun getIntProperty(oid: Oid, name: String): Int =
            getStatement(oid, name).executeSimpleQuery()?.get("value")?.getInt() ?: 0

    override fun setLongProperty(oid: Oid, name: String, value: Long) =
            setStatement(oid, name).bindLong(2, value).executeStatement()

    override fun getLongProperty(oid: Oid, name: String): Long =
            getStatement(oid, name).executeSimpleQuery()?.get("value")?.getLong() ?: 0

    override fun setRealProperty(oid: Oid, name: String, value: Double) =
            setStatement(oid, name).bindReal(2, value).executeStatement()

    override fun getRealProperty(oid: Oid, name: String): Double =
            getStatement(oid, name).executeSimpleQuery()?.get("value")?.getReal() ?: 0.0

    override fun setStringProperty(oid: Oid, name: String, value: String) =
            setStatement(oid, name).bindString(2, value).executeStatement()

    override fun getStringProperty(oid: Oid, name: String): String =
            getStatement(oid, name).executeSimpleQuery()?.get("value")?.getString() ?: ""

    override fun getSetProperty(oid: Oid, name: String): SetProperty =
            object : SetProperty {
                override fun add(item: Oid): SetProperty {
                    database.sqlStatement("INSERT OR IGNORE INTO prop_$name (oid, value) VALUES (?, ?)")
                            .bindInt(1, oid)
                            .bindOid(2, item)
                            .executeStatement()
                    return this
                }

                override fun remove(item: Oid): SetProperty {
                    database.sqlStatement("DELETE FROM prop_$name WHERE oid = ? AND value = ?")
                            .bindInt(1, oid)
                            .bindOid(2, item)
                            .executeStatement()
                    return this
                }

                override fun clear(): SetProperty {
                    database.sqlStatement("DELETE FROM prop_$name WHERE oid = ?")
                            .bindInt(1, oid)
                            .executeStatement()
                    return this
                }

                override fun contains(item: Oid): Boolean =
                        database.sqlStatement(
                                "SELECT COUNT(*) AS count FROM prop_$name WHERE oid = ? AND value = ? LIMIT 1")
                                .bindInt(1, oid)
                                .bindOid(2, item)
                                .executeSimpleQuery()!!
                                .getValue("count")
                                .getInt() != 0

                override fun getAll(): List<Oid> =
                        database.sqlStatement("SELECT value FROM prop_$name WHERE oid = ?")
                                .bindInt(1, oid)
                                .executeQuery()
                                .map { it.getValue("value").getOid()!! }

                override fun getOne(): Oid? =
                        database.sqlStatement("SELECT value FROM prop_$name WHERE oid = ? LIMIT 1")
                                .bindInt(1, oid)
                                .executeSimpleQuery()
                                ?.getValue("value")
                                ?.getOid()
            }
}
