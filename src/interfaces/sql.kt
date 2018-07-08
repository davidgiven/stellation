package interfaces

class SqlException(message: String) : Exception(message)

interface SqlValue {
    fun isNull(): Boolean
    fun getInt(): Int
    fun getLong(): Long
    fun getReal(): Double
    fun getString(): String
    fun getOid(): Oid?
}

interface SqlStatement {
    fun reset(): SqlStatement
    fun bindInt(index: Int, value: Int): SqlStatement
    fun bindLong(index: Int, value: Long): SqlStatement
    fun bindReal(index: Int, value: Double): SqlStatement
    fun bindString(index: Int, value: String): SqlStatement
    fun bindOid(index: Int, value: Oid?): SqlStatement
    fun executeQuery(): List<Map<String, SqlValue>>
    fun executeStatement()

    fun executeSimpleQuery(): Map<String, SqlValue>? = executeQuery().firstOrNull()
}

interface IDatabase {
    fun openDatabase(filename: String)
    fun closeDatabase()

    fun sqlStatement(sql: String): SqlStatement

    fun executeSql(sql: String) {
        sqlStatement(sql).executeStatement()
    }
}

fun IDatabase.withSqlTransaction(callback: () -> Unit) {
    this.executeSql("BEGIN")
    try {
        callback()
        this.executeSql("COMMIT")
    } catch (t: Throwable) {
        this.executeSql("ROLLBACK")
        throw t
    }
}
