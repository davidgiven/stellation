package datastore

class SqlException(message: String): Exception(message)

interface SqlValue {
    fun isNull(): Boolean
    fun getInt(): Int
    fun getReal(): Double
    fun getString(): String
}

interface SqlStatement {
    fun reset(): SqlStatement
    fun bindInt(index: Int, value: Int): SqlStatement
    fun bindReal(index: Int, value: Double): SqlStatement
    fun bindString(index: Int, value: String): SqlStatement
    fun executeQuery(): List<Map<String, SqlValue>>
    fun executeStatement()

    fun executeSimpleQuery(): Map<String, SqlValue>? = executeQuery().firstOrNull()
}

expect fun openDatabase(filename: String)
expect fun closeDatabase()

expect fun sqlStatement(sql: String): SqlStatement

fun executeSql(sql: String) {
    sqlStatement(sql).executeStatement()
}
