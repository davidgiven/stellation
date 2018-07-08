package runtime.shared

import datastore.IDatabase
import interfaces.IDatastore
import utils.get
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractDatastoreTest {
    protected val database get() = get<IDatabase>()
    protected val datastore get() = get<IDatastore>()

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

    @Test
    fun multipleSetReferencesShareData() {
        val o = datastore.createObject()
        val p1 = datastore.getSetProperty(o, "set")
        val p2 = datastore.getSetProperty(o, "set")
        var c = List(5) { datastore.createObject() }
        c.forEach { p1.add(it) }

        assertEquals(p1.getAll(), p2.getAll())
    }
}
