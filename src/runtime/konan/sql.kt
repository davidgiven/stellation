package runtime.konan

import interfaces.IDatabase
import interfaces.Oid
import interfaces.SqlException
import interfaces.SqlStatement
import interfaces.SqlValue
import sqlite3.*
import kotlinx.cinterop.*

private typealias SqliteConnection = CPointer<sqlite3>

private fun SqliteConnection.getSqliteError() = sqlite3_errmsg(this)!!.toKString()

private class SqliteValue(unprotectedValue: CPointer<sqlite3_value>?) : SqlValue {
    val value = sqlite3_value_dup(unprotectedValue)

    protected fun finalize() {
        sqlite3_value_free(value)
    }

    override fun isNull() = (value == null)
    override fun getInt(): Int = sqlite3_value_int(value)
    override fun getLong(): Long = sqlite3_value_int64(value)
    override fun getReal(): Double = sqlite3_value_double(value)
    override fun getString(): String = sqlite3_value_text(value)!!.toKString()
    override fun getOid(): Oid? = if (value == null) value else getInt()
}

private class SqliteStatement(val databaseConnection: SqliteConnection, val sql: String)
    : SqlStatement {
    var sqliteStatement: CPointer<sqlite3_stmt>? = null

    init {
        memScoped {
            val tailPtr = alloc<CPointerVar<ByteVar>>()
            val stmtPtr = alloc<CPointerVar<sqlite3_stmt>>()

            if (sqlite3_prepare_v2(databaseConnection, sql, -1, stmtPtr.ptr, tailPtr.ptr) != 0) {
                throw SqlException(
                        "Could not prepare statement: ${databaseConnection.getSqliteError()}: '${sql}'")
            }
            val tail = tailPtr.value!!.toKString()
            if (!tail.isEmpty()) {
                throw SqlException("Some of statement ignored: '${tail}'")
            }

            sqliteStatement = stmtPtr.value
        }
    }

    fun close() {
        sqlite3_finalize(sqliteStatement)
    }

    override fun reset(): SqliteStatement {
        sqlite3_reset(sqliteStatement)
        return this
    }

    override fun bindInt(index: Int, value: Int): SqliteStatement {
        sqlite3_bind_int(sqliteStatement, index, value)
        return this
    }

    override fun bindLong(index: Int, value: Long): SqliteStatement {
        sqlite3_bind_int64(sqliteStatement, index, value)
        return this
    }

    override fun bindReal(index: Int, value: Double): SqliteStatement {
        sqlite3_bind_double(sqliteStatement, index, value)
        return this
    }

    override fun bindString(index: Int, value: String): SqliteStatement {
        sqlite3_bind_text(sqliteStatement, index, value, -1, (-1L).toCPointer())
        return this
    }

    override fun bindOid(index: Int, value: Oid?): SqliteStatement {
        if (value == null) {
            sqlite3_bind_null(sqliteStatement, index)
        } else {
            sqlite3_bind_int(sqliteStatement, index, value)
        }
        return this
    }

    override fun executeQuery(): List<Map<String, SqlValue>> {
        val columnCount = sqlite3_column_count(sqliteStatement)
        var columnNames = Array<String>(columnCount) { "" }
        for (i in 0..(columnCount - 1)) {
            columnNames[i] = sqlite3_column_name(sqliteStatement, i)!!.toKString()
        }

        fun generator(): Map<String, SqlValue>? {
            val e = sqlite3_step(sqliteStatement)
            when (e) {
                SQLITE_DONE -> return null
                SQLITE_ROW  -> {
                    var data: Map<String, SqlValue> = emptyMap()
                    for (i in 0..(columnCount - 1)) {
                        data += Pair(columnNames[i], SqliteValue(sqlite3_column_value(sqliteStatement, i)))
                    }
                    return data
                }
                else        ->
                    throw SqlException("Cursor error $e")
            }
        }

        return generateSequence(::generator).toList()
    }

    override fun executeStatement() {
        val e = sqlite3_step(sqliteStatement)
        if (e != SQLITE_DONE) {
            throw SqlException("Statement execution error $e")
        }
    }
}

class KonanDatabase : IDatabase {
    private var databaseConnection: SqliteConnection? = null
    private var statementCache: Map<String, SqliteStatement> = emptyMap()

    override fun openDatabase(filename: String) {
        check(databaseConnection == null)
        memScoped {
            val dbPtr = alloc<CPointerVar<sqlite3>>()
            if (sqlite3_open(filename, dbPtr.ptr) != 0) {
                throw SqlException("Cannot open db: ${sqlite3_errmsg(dbPtr.value)}")
            }
            databaseConnection = dbPtr.value
        }

        executeSql("PRAGMA encoding = \"UTF-8\"")
        executeSql("PRAGMA synchronous = OFF")
        executeSql("PRAGMA foreign_keys = ON")
        executeSql("PRAGMA temp_store = MEMORY")
    }

    override fun closeDatabase() {
        check(databaseConnection != null)
        statementCache.values.forEach { it.close() }
        statementCache = emptyMap()
        sqlite3_close_v2(databaseConnection)
        databaseConnection = null
    }

    override fun sqlStatement(sql: String): SqlStatement {
        var statement = statementCache.get(sql)
        if (statement == null) {
            statement = SqliteStatement(databaseConnection!!, sql)
            statementCache += Pair(sql, statement)
        }
        return statement.reset()
    }
}
