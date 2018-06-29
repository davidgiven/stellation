package datastore

import ksqlite.KSqlite
import ksqlite.KStatement

var databaseConnection: KSqlite? = null
var statementCache: Map<String, KStatement> = emptyMap()

actual fun openDatabase(filename: String) {
    check(databaseConnection == null)
    databaseConnection = KSqlite(filename)
}

fun closeDatabase() {
    check(databaseConnection != null)
    statementCache.values.forEach { it.close() }
    statementCache = emptyMap()
    databaseConnection!!.close()
    databaseConnection = null
}

actual fun executeSql(sql: String) {
    var statement = statementCache.get(sql)
    if (statement == null) {
        statement = databaseConnection!!.prepare(sql)
        statementCache += Pair(sql, statement)
    }
    statement.execute()
}

