package datastore

import utils.UNIMPLEMENTED

actual fun openDatabase(filename: String): Unit = UNIMPLEMENTED()
actual fun closeDatabase() {}
actual fun sqlStatement(sql: String): SqlStatement = UNIMPLEMENTED()

