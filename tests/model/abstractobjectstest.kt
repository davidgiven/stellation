package model

import interfaces.IDatabase
import interfaces.IDatastore
import utils.get
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.fail

abstract class AbstractObjectsTest {
    protected val database get() = get<IDatabase>()
    protected val datastore get() = get<IDatastore>()
    protected val model get() = get<Model>()

    @Test
    fun objectCreationTest() {
        val universe = model.createObject(SUniverse::class)
        assertEquals(1, universe.oid)
        assertEquals(SUniverse::class.simpleName, universe.kind)

        val star = model.createObject(SStar::class)
        assertEquals(2, star.oid)
        assertEquals(SStar::class.simpleName, star.kind)
    }

    @Test fun propertySetGetTest() {
        var s = model.createObject(SStar::class)
        assertEquals(SStar::class.simpleName, s.kind)

        s.name = "Foo"
        assertEquals("Foo", s.name)
        s.name = "Bar"
        assertEquals("Bar", s.name)

        s.brightness = 7.6
        assertEquals(7.6, s.brightness)

        s.asteroidsM = 42
        assertEquals(42, s.asteroidsM)
    }

    @Test
    fun objectSaveAndLoadTest() {
        val s1 = model.createObject(SStar::class)
        s1.name = "Foo"
        s1.brightness = 7.6
        s1.asteroidsM = 42

        val s2 = model.loadRawObject(s1.oid, SStar::class)
        assertNotSame(s2, s1)
        assertEquals("Foo", s2.name)
        assertEquals(7.6, s2.brightness)
        assertEquals(42, s2.asteroidsM)
    }

    @Test
    fun objectEqualityTest() {
        val s1 = model.createObject(SStar::class)
        val s2 = model.loadRawObject(s1.oid, SStar::class)

        assertNotSame(s2, s1)
        assertEquals(s2, s1)
    }

    @Test
    fun objectCacheTest() {
        val g1 = model.createObject(SGalaxy::class)
        val g2 = model.loadObject(g1.oid, SGalaxy::class)
        assertSame(g2, g1)
    }

    @Test
    fun objectDoesNotExistTest() {
        try {
            model.loadRawObject(42, SStar::class)
            fail()
        } catch (_: ObjectNotVisibleException) {
        }
    }

    @Test
    fun refTest() {
        var u = model.createObject(SUniverse::class)
        var g = model.createObject(SGalaxy::class)

        assertNotEquals(u.oid, g.oid)
        assertNull(u.galaxy)
        u.galaxy = g
        assertEquals(g.oid, u.galaxy!!.oid)

        u.galaxy = null
        assertNull(u.galaxy)
    }

    @Test
    fun downcastTest() {
        var f = model.createObject(SFactory::class)
        var m = model.loadRawObject(f.oid, SModule::class)

        assertEquals(m.kind, f.kind)
    }

    @Test
    fun upcastTest() {
        try {
            var m = model.createObject(SModule::class)
            model.loadRawObject(m.oid, SFactory::class)
        } catch (_: DatabaseTypeMismatchException) {
        }
    }
}
