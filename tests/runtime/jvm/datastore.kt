package runtime.jvm

import datastore.IDatabase
import interfaces.IContext
import interfaces.IDatastore
import interfaces.context
import runtime.jvmkonan.SqlDatastore
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DatastoreTest {
    private val database get() = context.database!!
    private val datastore get() = context.datastore!!


    @BeforeTest
    fun setup() {
        context = object : IContext() {
            override val database = JvmDatabase()
            override val datastore = SqlDatastore(database)
        }

        database.openDatabase(":memory:")
    }

    @AfterTest
    fun teardown() {
        context.database!!.closeDatabase()
    }

    @Test
    fun objectCreationTest() {
        var o1 = datastore.createObject()
        assertEquals(1, o1)

        var o2 = datastore.createObject()
        assertEquals(2, o2)
    }
}
