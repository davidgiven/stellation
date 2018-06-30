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
    fun bindInt(name: String, value: Int?): SqlStatement
    fun bindReal(name: String, value: Double?): SqlStatement
    fun bindString(name: String, value: String?): SqlStatement
    fun execute(): List<Map<String, SqlValue>>
}

expect fun openDatabase(filename: String)
expect fun closeDatabase()

expect fun sqlStatement(sql: String): SqlStatement

fun executeSql(sql: String) {
    sqlStatement(sql).execute()
}
