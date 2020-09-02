package runtime.cpp;

import runtime.cpp.Sqlite;

class Database {
    private var db: SqliteDatabase;

    public function new() {}

    public function openDatabase(filename: String) {
		db = Sqlite.open(filename);
//        
//
//        memScoped {
//            val dbPtr = alloc<CPointerVar<sqlite3>>()
//            if (sqlite3_open(filename, dbPtr.ptr) != 0) {
//                throw SqlException("Cannot open db: ${sqlite3_errmsg(dbPtr.value)}")
//            }
//            databaseConnection = dbPtr.value
//        }
//
//        executeSql("PRAGMA encoding = \"UTF-8\"")
//        executeSql("PRAGMA synchronous = OFF")
//        executeSql("PRAGMA foreign_keys = ON")
//        executeSql("PRAGMA temp_store = MEMORY")
//    }
//
//    override fun closeDatabase() {
//        check(databaseConnection != null)
//        statementCache.values.forEach { it.close() }
//        statementCache = emptyMap()
//        sqlite3_close_v2(databaseConnection)
//        databaseConnection = null
//    }
    }
}

