package runtime.jvm

import datastore.IDatabase
import datastore.Oid
import datastore.SqlStatement
import datastore.SqlValue
import org.sqlite.JDBC
import org.sqlite.SQLiteConfig
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.Types

private class SqliteValue(val o: String?) : SqlValue {
    override fun isNull(): Boolean = (o == null)

    override fun getInt(): Int = o!!.toInt()
    override fun getLong(): Long = o!!.toLong()
    override fun getReal(): Double = o!!.toDouble()
    override fun getString(): String = o!!
    override fun getOid(): Oid? = o?.toInt()
}

private class SqliteStatement(val sqliteStatement: PreparedStatement) : SqlStatement {
    fun close() {
        sqliteStatement.close()
    }

    override fun reset(): SqliteStatement {
        sqliteStatement.clearParameters()
        return this
    }

    override fun bindInt(index: Int, value: Int): SqliteStatement {
        sqliteStatement.setInt(index, value)
        return this
    }

    override fun bindLong(index: Int, value: Long): SqliteStatement {
        sqliteStatement.setLong(index, value)
        return this
    }

    override fun bindReal(index: Int, value: Double): SqliteStatement {
        sqliteStatement.setDouble(index, value)
        return this
    }

    override fun bindString(index: Int, value: String): SqliteStatement {
        sqliteStatement.setString(index, value)
        return this
    }

    override fun bindOid(index: Int, value: Oid?): SqliteStatement {
        if (value == null) {
            sqliteStatement.setNull(index, Types.INTEGER)
        } else {
            sqliteStatement.setInt(index, value)
        }
        return this
    }

    override fun executeQuery(): List<Map<String, SqlValue>> {
        val results = sqliteStatement.executeQuery()
        val metadata = results.metaData
        val columnCount = metadata.columnCount
        var columnNames = Array<String>(columnCount) { metadata.getColumnLabel(it + 1) }

        fun generator(): Map<String, SqlValue>? {
            if (!results.next()) {
                return null
            }

            var data: Map<String, SqlValue> = emptyMap()
            for (i in 0..(columnCount - 1)) {
                data += Pair(columnNames[i], SqliteValue(results.getString(i + 1)))
            }
            return data
        }

        try {
            return generateSequence(::generator).toList()
        } finally {
            reset()
        }
    }

    override fun executeStatement() {
        sqliteStatement.execute()
    }
}

class JvmDatabase : IDatabase {
    private var databaseConnection: Connection? = null
    private var statementCache: Map<String, SqliteStatement> = emptyMap()

    override fun openDatabase(filename: String) {
        var config = SQLiteConfig()
        config.setEncoding(SQLiteConfig.Encoding.UTF8)
        config.setSynchronous(SQLiteConfig.SynchronousMode.OFF)
        config.enforceForeignKeys(true)
        config.setTempStore(SQLiteConfig.TempStore.MEMORY)

        DriverManager.registerDriver(JDBC())
        databaseConnection = DriverManager.getConnection("jdbc:sqlite:$filename", config.toProperties())
        databaseConnection!!.autoCommit = false
        executeSql("COMMIT") // Why does turning autocommit off leave a transaction open?
    }

    override fun closeDatabase() {
        statementCache.values.forEach { it.close() }
        statementCache = emptyMap()
        databaseConnection!!.close()
        databaseConnection = null
    }

    override fun sqlStatement(sql: String): SqlStatement {
        var statement = statementCache.get(sql)
        if (statement == null) {
            statement = SqliteStatement(databaseConnection!!.prepareStatement(sql))

            statementCache += Pair(sql, statement)
        }
        return statement
    }
}
