package runtime.jvm

import datastore.withSqlTransaction
import interfaces.IContext
import interfaces.context
import runtime.jvmkonan.SqlDatastore
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.Test

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

    @Test
    fun objectCreationTest() {
        var o1 = datastore.createObject()
        assertEquals(1, o1)

        var o2 = datastore.createObject()
        assertEquals(2, o2)
    }

    @Test
    fun intSetGetTest() {
        var o = datastore.createObject()
        datastore.setIntProperty(o, "integer", 5)
        var i = datastore.getIntProperty(o, "integer")
        assertEquals(5, i)
    }

    @Test
    fun longSetGetTest() {
        var o = datastore.createObject()
        datastore.setLongProperty(o, "integer", 5)
        var i = datastore.getLongProperty(o, "integer")
        assertEquals(5, i)
    }

    @Test
    fun stringSetGetTest() {
        var o = datastore.createObject()
        datastore.setStringProperty(o, "integer", "fnord")
        var s = datastore.getStringProperty(o, "integer")
        assertEquals("fnord", s)
    }

    @Test
    fun addItemsToSetTest() {
        val o = datastore.createObject()
        val p = datastore.getSetProperty(o, "set")
        val c = List(5) { datastore.createObject() }
        c.forEach { p.add(it) }

        assertEquals(c, p.getAll())
    }

    @Test
    fun removeItemsFromSetTest() {
        val o = datastore.createObject()
        val p = datastore.getSetProperty(o, "set")
        val c = List(5) { datastore.createObject() }
        c.forEach { p.add(it) }

        p.remove(c[2])

        assertEquals(listOf(c[0], c[1], c[3], c[4]), p.getAll())
    }

    @Test
    fun getItemFromSetTest() {
        val o = datastore.createObject()
        val p = datastore.getSetProperty(o, "set")
        var c = List(5) { datastore.createObject() }
        c.forEach { p.add(it) }

        while (true) {
            var s = p.getOne()
            s ?: break

            assertTrue(c.contains(s))
            p.remove(s)
            c -= s
        }

        assertTrue(c.isEmpty())
    }

    @Test
    fun clearSetTest() {
        val o = datastore.createObject()
        val p = datastore.getSetProperty(o, "set")
        var c = List(5) { datastore.createObject() }
        c.forEach { p.add(it) }

        p.clear()

        assertEquals(0, p.getAll().size)
    }
}
