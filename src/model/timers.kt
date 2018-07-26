package model

import interfaces.Oid
import interfaces.IDatabase
import utils.injection

class Timers() {
    val model by injection<Model>()
    val database by injection<IDatabase>()

    fun setTimer(oid: Oid, expiry: Long) {
        database.sqlStatement("INSERT OR REPLACE INTO timers (oid, expiry) VALUES (?, ?)")
                .bindOid(1, oid)
                .bindLong(2, expiry)
                .executeStatement()
    }

    fun processTimers(max_time: Long, callback: (Oid, Long) -> Unit) {
        while (true) {
            val results = database.sqlStatement(
                    """
                    SELECT oid, expiry FROM timers
                    WHERE expiry < ?
                    ORDER BY expiry ASC
                    LIMIT 1
                """)
                    .bindLong(1, max_time)
                    .executeSimpleQuery()
            results ?: break

            val oid = results.get("oid")!!.getInt()
            val expiry = results.get("expiry")!!.getLong()

            database.sqlStatement("DELETE FROM timers WHERE oid = ?")
                    .bindOid(1, oid)
                    .executeStatement()

            callback(oid, expiry)
        }
    }
}
