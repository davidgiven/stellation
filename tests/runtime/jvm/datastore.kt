package runtime.jvm

import interfaces.IContext
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
        datastore.initialiseDatabase()
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

//    @Test
//    fun intSetGetTest() {
//        var o = datastore.createObject()
//        datastore.setIntProperty(o, "asteroids_m", 5)
//        var i = datastore.getIntProperty(o, "asteroids_m")
//        assertEquals(5, i)
//    }
}
