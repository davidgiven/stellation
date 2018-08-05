package runtime.shared

import interfaces.IClock
import interfaces.IDatabase
import interfaces.IDatastore
import utils.Oid
import interfaces.SetProperty
import utils.injection

class SqlDatastore : IDatastore {
    val database by injection<IDatabase>()
    val clock by injection<IClock>()

    override fun initialiseDatabase() {
        database.executeSql(
                """
                CREATE TABLE IF NOT EXISTS objects (
                    oid INTEGER PRIMARY KEY AUTOINCREMENT
                )
            """)

        database.executeSql(
                """
                CREATE TABLE IF NOT EXISTS mtimes (
                    oid INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                    name TEXT,
                    mtime REAL,
                    PRIMARY KEY(oid, name)
                )
            """)
        database.executeSql(
                """
                CREATE INDEX IF NOT EXISTS mtimes_by_mtime ON mtimes (mtime ASC)
            """)
    }

    override fun createProperty(name: String, sqlType: String, isAggregate: Boolean) {
        if (!isAggregate) {
            database.executeSql(
                    """
                 CREATE TABLE IF NOT EXISTS prop_$name (
                    oid INTEGER PRIMARY KEY NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                    value $sqlType)
            """)
        } else {
            database.executeSql(
                    """
                 CREATE TABLE IF NOT EXISTS prop_$name (
                    oid INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                    value $sqlType)
            """)
        }
    }

    override fun createObject(): Oid {
        database.sqlStatement("INSERT INTO objects (oid) VALUES(NULL)")
                .executeStatement()
        return database.sqlStatement("SELECT last_insert_rowid() AS oid")
                .executeSimpleQuery()!!
                .getValue("oid")
                .getOid()!!

    }

    override fun createObject(oid: Oid) {
        database.sqlStatement("INSERT INTO objects (oid) VALUES(?)")
                .bindOid(1, oid)
                .executeStatement()
    }

    override fun destroyObject(oid: Oid) {
        database.sqlStatement("DELETE FROM objects WHERE oid = ?")
                .bindInt(1, oid)
                .executeStatement()
    }

    override fun getAllObjects() =
            database.sqlStatement("SELECT oid FROM objects")
                    .executeQuery()
                    .map { it.get("oid")!!.getOid()!! }

    override fun doesObjectExist(oid: Oid): Boolean =
            database.sqlStatement("SELECT COUNT(*) AS count FROM objects WHERE oid = ? LIMIT 1")
                    .bindOid(1, oid)
                    .executeSimpleQuery()!!
                    .getValue("count")
                    .getInt() != 0

    private fun propertyChanged(oid: Oid, name: String) {
        database.sqlStatement(
                """
                        INSERT OR REPLACE INTO mtimes (oid, name, mtime) VALUES (?, ?, ?)
                    """)
                .bindOid(1, oid)
                .bindString(2, name)
                .bindReal(3, clock.getTime())
                .executeStatement()
    }

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

    override fun setOidProperty(oid: Oid, name: String, value: Oid?) {
        setStatement(oid, name).bindOid(2, value).executeStatement()
        propertyChanged(oid, name)
    }

    override fun getOidProperty(oid: Oid, name: String): Oid? =
            getStatement(oid, name).executeSimpleQuery()?.get("value")?.getOid()

    override fun setIntProperty(oid: Oid, name: String, value: Int) {
        setStatement(oid, name).bindInt(2, value).executeStatement()
        propertyChanged(oid, name)
    }

    override fun getIntProperty(oid: Oid, name: String): Int =
            getStatement(oid, name).executeSimpleQuery()?.get("value")?.getInt() ?: 0

    override fun setLongProperty(oid: Oid, name: String, value: Long) {
        setStatement(oid, name).bindLong(2, value).executeStatement()
        propertyChanged(oid, name)
    }

    override fun getLongProperty(oid: Oid, name: String): Long =
            getStatement(oid, name).executeSimpleQuery()?.get("value")?.getLong() ?: 0

    override fun setRealProperty(oid: Oid, name: String, value: Double) {
        setStatement(oid, name).bindReal(2, value).executeStatement()
        propertyChanged(oid, name)
    }

    override fun getRealProperty(oid: Oid, name: String): Double =
            getStatement(oid, name).executeSimpleQuery()?.get("value")?.getReal() ?: 0.0

    override fun setStringProperty(oid: Oid, name: String, value: String) {
        setStatement(oid, name).bindString(2, value).executeStatement()
        propertyChanged(oid, name)
    }

    override fun getStringProperty(oid: Oid, name: String): String =
            getStatement(oid, name).executeSimpleQuery()?.get("value")?.getString() ?: ""

    override fun getSetProperty(oid: Oid, name: String): SetProperty =
            object : SetProperty {
                override fun add(item: Oid): SetProperty {
                    database.sqlStatement("INSERT OR IGNORE INTO prop_$name (oid, value) VALUES (?, ?)")
                            .bindInt(1, oid)
                            .bindOid(2, item)
                            .executeStatement()
                    propertyChanged(oid, name)
                    return this
                }

                override fun remove(item: Oid): SetProperty {
                    database.sqlStatement("DELETE FROM prop_$name WHERE oid = ? AND value = ?")
                            .bindInt(1, oid)
                            .bindOid(2, item)
                            .executeStatement()
                    propertyChanged(oid, name)
                    return this
                }

                override fun clear(): SetProperty {
                    database.sqlStatement("DELETE FROM prop_$name WHERE oid = ?")
                            .bindInt(1, oid)
                            .executeStatement()
                    propertyChanged(oid, name)
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

    override fun getPropertiesChangedSince(oids: List<Oid>, timestamp: Double): List<Pair<Oid, String>> {
        database.executeSql(
                """
                    DROP TABLE IF EXISTS _temp_oids
                """
        )
        database.executeSql(
                """
                    CREATE TEMPORARY TABLE _temp_oids (
                        oid INTEGER PRIMARY KEY
                    )
                """)
        for (oid in oids) {
            database.sqlStatement(
                    """
                        INSERT INTO _temp_oids (oid) VALUES (?)
                    """)
                    .bindOid(1, oid)
                    .executeStatement()
        }

        return database.sqlStatement(
                """
                    SELECT
                        oid, name
                    FROM
                        mtimes
                    WHERE
                        (mtime > ?)
                        AND EXISTS (SELECT * FROM _temp_oids WHERE _temp_oids.oid = mtimes.oid)
                """)
                .bindReal(1, timestamp)
                .executeQuery()
                .map { Pair(it.get("oid")!!.getInt(), it.get("name")!!.getString()) }
    }

    override fun getHierarchy(root: Oid, containment: String): Set<Oid> =
            database.sqlStatement(
                    """
                    WITH RECURSIVE
                        children(child) AS (
                            SELECT ?
                            UNION ALL
                            SELECT value
                                FROM prop_$containment, children
                                ON oid == children.child
                        )
                    SELECT child FROM children
                """
            ).bindOid(1, root)
                    .executeQuery()
                    .map { it.getValue("child").getOid()!! }
                    .toSet()
}
