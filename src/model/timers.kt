package model

import interfaces.IDatabase
import interfaces.Oid
import utils.injection

class Timers {
    val model by injection<Model>()
    val database by injection<IDatabase>()

    fun initialiseDatabase() {
        database.executeSql(
                """
                CREATE TABLE IF NOT EXISTS timers (
                    oid INTEGER PRIMARY KEY NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                    expiry INTEGER
                )
            """)
        database.executeSql(
                """
                CREATE INDEX IF NOT EXISTS timers_by_expiry ON timers (expiry ASC)
            """)
    }

    fun setTimer(oid: Oid, expiry: Double) {
        database.sqlStatement("INSERT OR REPLACE INTO timers (oid, expiry) VALUES (?, ?)")
                .bindOid(1, oid)
                .bindReal(2, expiry)
                .executeStatement()
    }

    fun processTimers(max_time: Double, callback: (Oid, Double) -> Unit) {
        while (true) {
            val results = database.sqlStatement(
                    """
                    SELECT oid, expiry FROM timers
                    WHERE expiry < ?
                    ORDER BY expiry ASC
                    LIMIT 1
                """)
                    .bindReal(1, max_time)
                    .executeSimpleQuery()
            results ?: break

            val oid = results.get("oid")!!.getInt()
            val expiry = results.get("expiry")!!.getReal()

            database.sqlStatement("DELETE FROM timers WHERE oid = ?")
                    .bindOid(1, oid)
                    .executeStatement()

            callback(oid, expiry)
        }
    }
}
