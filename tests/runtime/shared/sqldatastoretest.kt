package runtime.shared

import datastore.withSqlTransaction
import interfaces.IContext
import interfaces.context
import runtime.jvm.JvmDatabase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class SqlDatastoreTest : AbstractDatastoreTest() {
    @BeforeTest
    fun setup() {
        context = object : IContext() {
            override val database = JvmDatabase()
            override val datastore = SqlDatastore(database)
        }

        database.openDatabase(":memory:")
        datastore.initialiseDatabase()
        database.withSqlTransaction {
            for (t in listOf("INTEGER", "REAL", "TEXT")) {
                datastore.createProperty(t.toLowerCase(), t)
            }
            datastore.createProperty(
                    "set",
                    "INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE")
        }
    }

    @AfterTest
    fun teardown() {
        context.database!!.closeDatabase()
    }
}
