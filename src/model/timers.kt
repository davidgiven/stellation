package model

import interfaces.IDatabase
import utils.Oid
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

    fun getOldestTimer(maxTime: Double): Pair<Oid, Double>? {
        val results = database.sqlStatement(
                """
                    SELECT oid, expiry FROM timers
                    WHERE expiry < ?
                    ORDER BY expiry ASC
                    LIMIT 1
                """)
                .bindReal(1, maxTime)
                .executeSimpleQuery()
        results ?: return null

        return Pair(results.get("oid")!!.getOid()!!, results.get("expiry")!!.getReal())
    }

    fun processTimers(maxTime: Double, callback: (Oid, Double) -> Unit) {
        while (true) {
            val (oid, expiry) = getOldestTimer(maxTime) ?: break

            database.sqlStatement("DELETE FROM timers WHERE oid = ?")
                    .bindOid(1, oid)
                    .executeStatement()

            callback(oid, expiry)
        }
    }
}
