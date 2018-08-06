package runtime.shared

import interfaces.IClock
import interfaces.IDatabase
import interfaces.IDatastore
import utils.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

abstract class AbstractDatastoreTest {
    protected val clock get() = inject<IClock>()
    protected val database get() = inject<IDatabase>()
    protected val datastore get() = inject<IDatastore>()

    protected fun createProperties() {
        for (t in listOf("INTEGER", "REAL", "TEXT")) {
            datastore.createProperty(t.toLowerCase(), t, false)
        }
        datastore.createProperty(
                "oid",
                "INTEGER REFERENCES objects(oid) ON DELETE CASCADE",
                false)
        datastore.createProperty(
                "set",
                "INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE",
                true)
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
    fun hasPropertyTest() {
        var o = datastore.createObject()
        assertFalse(datastore.hasProperty(o, "integer"))

        datastore.setIntProperty(o, "integer", 5)
        assertTrue(datastore.hasProperty(o, "integer"))
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

    @Test
    fun oidNulls() {
        var o = datastore.createObject()
        assertEquals(null, datastore.getOidProperty(o, "oid"))
        datastore.setOidProperty(o, "oid", o)
        assertEquals(o, datastore.getOidProperty(o, "oid"))
        datastore.setOidProperty(o, "oid", null)
        assertEquals(null, datastore.getOidProperty(o, "oid"))
    }

    @Test
    fun hierarchyTest() {
        var o = datastore.createObject()
        var o1 = datastore.createObject()
        datastore.getSetProperty(o, "set").add(o1)
        var o2 = datastore.createObject()
        datastore.getSetProperty(o, "set").add(o2)
        var o21 = datastore.createObject()
        datastore.getSetProperty(o2, "set").add(o21)
        var o22 = datastore.createObject()
        datastore.getSetProperty(o2, "set").add(o22)

        assertEquals(setOf(o, o1, o2, o21, o22), datastore.getHierarchy(o, "set"))
        assertEquals(setOf(o2, o21, o22), datastore.getHierarchy(o2, "set"))
        assertEquals(setOf(o1), datastore.getHierarchy(o1, "set"))
    }
}
