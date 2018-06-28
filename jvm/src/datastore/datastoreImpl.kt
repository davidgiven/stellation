package datastore

import org.sqlite.JDBC
import java.sql.Connection
import java.sql.DriverManager

var databaseConnection: Connection? = null

actual fun openDatabase(filename: String) {
    DriverManager.registerDriver(JDBC())
    databaseConnection = DriverManager.getConnection("jdbc:sqlite:$filename")
}

actual fun executeSql(sql: String) {
    databaseConnection!!.createStatement().use {
        it.execute(sql)
    }
}

