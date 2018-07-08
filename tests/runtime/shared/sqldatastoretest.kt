package runtime.shared

import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.withSqlTransaction
import runtime.jvm.JvmDatabase
import utils.bind
import utils.resetBindingsForTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class SqlDatastoreTest : AbstractDatastoreTest() {
    @BeforeTest
    fun setup() {
        resetBindingsForTest()
        bind<IDatabase>(JvmDatabase())
        bind<IDatastore>(SqlDatastore(database))

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
        database.closeDatabase()
    }
}
