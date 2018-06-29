package ksqlite

import sqlite3.*
import kotlinx.cinterop.*

const val SQLITE3_ROW = 100
const val SQLITE3_DONE = 101

typealias DbConnection = CPointer<sqlite3>?

class KSqliteError(message: String) : Error(message)

private fun fromCArray(ptr: CPointer<CPointerVar<ByteVar>>, count: Int) =
        Array(count, { index -> (ptr + index)!!.pointed.value!!.toKString() })

class KStatement {
    var db: DbConnection
    var sqliteStatement: CPointer<sqlite3_stmt>? = null

    constructor(db: DbConnection, sql: String) {
        this.db = db

        memScoped {
            val tailPtr = alloc<CPointerVar<ByteVar>>()
            val stmtPtr = alloc<CPointerVar<sqlite3_stmt>>()

            if (sqlite3_prepare_v2(db, sql, -1, stmtPtr.ptr, tailPtr.ptr) != 0) {
                throw KSqliteError("Could not prepare statement: '${sql}'")
            }
            val tail = tailPtr.value!!.toKString()
            if (!tail.isEmpty()) {
                throw KSqliteError("Some of statement ignored: '${tail}'")
            }

            sqliteStatement = stmtPtr.value
        }
    }

    fun close() {
        sqlite3_finalize(sqliteStatement)
    }

    fun reset(): KStatement {
        sqlite3_reset(sqliteStatement)
        return this
    }

    fun bind(name: String, value: String?): KStatement {
        val index = sqlite3_bind_parameter_index(sqliteStatement, name)
        if (index == 0) {
            throw KSqliteError("Parameter '$name' does not exist in this query")
        }

        if (value == null) {
            sqlite3_bind_null(sqliteStatement, index)
        } else {
            sqlite3_bind_text(sqliteStatement, index, value, -1, (-1L).toCPointer())
        }

        return this
    }

    fun execute(callback: (Map<String, String?>) -> Unit = { _ -> }) {
        val columnCount = sqlite3_column_count(sqliteStatement)
        var columnNames = Array<String>(columnCount) { "" }
        for (i in 0..(columnCount - 1)) {
            columnNames[i] = sqlite3_column_origin_name(sqliteStatement, i)!!.toKString()
        }

        while (true) {
            val e = sqlite3_step(sqliteStatement)
            when (e) {
                SQLITE3_DONE -> return

                SQLITE3_ROW -> {
                    var data: Map<String, String?> = emptyMap()
                    for (i in 0..(columnCount - 1)) {
                        data += Pair(columnNames[i],
                                sqlite3_column_text(sqliteStatement, i)?.toKString())
                    }
                    callback(data)
                }

                else ->
                    throw KSqliteError("Error $e")
            }
        }

    }
}

class KSqlite {
    var dbPath: String = ""
    var db: DbConnection = null

    constructor(dbPath: String) {
        memScoped {
            val dbPtr = alloc<CPointerVar<sqlite3>>()
            if (sqlite3_open(dbPath, dbPtr.ptr) != 0) {
                throw KSqliteError("Cannot open db: ${sqlite3_errmsg(dbPtr.value)}")
            }
            db = dbPtr.value
        }
    }

    fun prepare(sql: String) = KStatement(db, sql)

    override fun toString(): String = "SQLite database in $dbPath"

    fun close() {
        if (db != null) {
            sqlite3_close(db)
            db = null
        }
    }
}

inline fun withSqlite(path: String, function: (KSqlite) -> Unit) {
    val db = KSqlite(path)
    try {
        function(db)
    } finally {
        db.close()
    }
}
