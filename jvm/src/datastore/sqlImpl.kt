package datastore

import org.sqlite.JDBC
import java.sql.Connection
import java.sql.DriverManager

private var databaseConnection: Connection? = null

actual fun openDatabase(filename: String) {
    DriverManager.registerDriver(JDBC())
    databaseConnection = DriverManager.getConnection("jdbc:sqlite:$filename")
}

actual fun closeDatabase() {
    databaseConnection!!.close()
    databaseConnection = null
}

actual fun sqlStatement(sql: String): SqlStatement {
    TODO("not implemented yet")
}
//actual fun executeSql(sql: String) {
//    databaseConnection!!.createStatement().use {
//        it.execute(sql)
//    }
//}

