package datastore

import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import model.DatabaseTypeMismatchException
import model.ObjectNotVisibleException
import model.SFactory
import model.SGalaxy
import model.SModule
import model.SStar
import model.SThing
import model.SUniverse
import model.createObject
import model.load
import model.loadRaw
import model.loadRawObject
import kotlin.test.assertTrue

class DatabaseTest {
    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Before
    fun setup() {
        openDatabase(":memory:")
        initialiseDatabase()
    }

    @After
    fun teardown() {
        closeDatabase()
    }

    @Test
    fun objectCreationTest() {
        val t1 = createObject(SUniverse::class)
        assertThat(t1.oid).isEqualTo(1)
        assertThat(t1.kind).isEqualTo("SUniverse")

        val t2 = createObject(SStar::class)
        assertThat(t2.oid).isEqualTo(2)
        assertThat(t2.kind).isEqualTo("SStar")
    }

    @Test
    fun propertySetGetTest() {
        var s = createObject(SStar::class)
        assertThat(s.kind).isEqualTo("SStar")

        s.name = "Foo"
        assertThat(s.name).isEqualTo("Foo")
        s.name = "Bar"
        assertThat(s.name).isEqualTo("Bar")

        s.brightness = 7.6
        assertThat(s.brightness).isEqualTo(7.6)

        s.asteroidsM = 42
        assertThat(s.asteroidsM).isEqualTo(42)
    }

    @Test
    fun objectSaveAndLoadTest() {
        val s1 = createObject(SStar::class)
        s1.name = "Foo"
        s1.brightness = 7.6
        s1.asteroidsM = 42

        val s2 = s1.oid.loadRaw(SStar::class)
        assertThat(s2.name).isEqualTo("Foo")
        assertThat(s2.brightness).isEqualTo(7.6)
        assertThat(s2.asteroidsM).isEqualTo(42)
    }

    @Test
    fun objectEqualityTest() {
        val s1 = createObject(SStar::class)
        val s2 = s1.oid.loadRaw(SStar::class)

        assertThat(s1).isEqualTo(s2)
    }

    @Test
    fun objectDoesNotExistTest() {
        thrown.expect(ObjectNotVisibleException::class.java)
        loadRawObject(42, SStar::class)
    }

    @Test
    fun refTest() {
        var u = createObject(SUniverse::class)
        var g = createObject(SGalaxy::class)

        assertThat(u.oid).isNotEqualTo(g.oid)
        assertThat(u.galaxy).isNull()
        u.galaxy = g
        assertThat(u.galaxy!!.oid).isEqualTo(g.oid)

        u.galaxy = null
        assertTrue(u.galaxy == null)
    }

    @Test
    fun downcastTest() {
        var f = createObject(SFactory::class)
        var m = f.oid.loadRaw(SModule::class)

        assertThat(f.kind).isEqualTo(m.kind)
    }

    @Test
    fun upcastTest() {
        thrown.expect(DatabaseTypeMismatchException::class.java)

        var m = createObject(SModule::class)
        m.oid.loadRaw(SFactory::class)
    }

    @Test
    fun addItemsToSetTest() {
        var g = createObject(SGalaxy::class)
        var stars = Array(5) { createObject(SStar::class) }
        stars.forEach { g.contents += it }

        assertThat(g.contents).containsExactlyElementsIn(stars)
    }

    @Test
    fun removeItemsFromSetTest() {
        var g = createObject(SGalaxy::class)
        var contents = Array(5) { createObject(SStar::class) }
        contents.forEach { g.contents += it }

        g.contents -= contents[2]

        assertThat(g.contents).containsExactly(contents[0], contents[1], contents[3], contents[4])
    }

    @Test
    fun getItemFromSetTest() {
        var g = createObject(SGalaxy::class)
        var stars = List<SThing>(5) { createObject(SStar::class) }
        stars.forEach { g.contents += it }

        while (true) {
            var s = g.contents.getOne()
            s ?: break

            assertThat(s).isIn(stars)
            g.contents -= s
            stars -= s
        }

        assertThat(stars).isEmpty()
    }

    @Test
    fun clearSetTest() {
        var g = createObject(SGalaxy::class)
        var contents = List(5) { createObject(SStar::class) }
        contents.forEach { g.contents += it }

        g.contents.clear()

        assertThat(g.contents).isEmpty()
    }

    @Test
    fun destroyObjectsTest() {
        var g = createObject(SGalaxy::class)
        var contents = List(5) { createObject(SStar::class) }
        contents.forEach { g.contents += it }

        val oid = contents[2].oid
        destroyObject(oid)

        assertThat( doesObjectExist(oid)).isFalse()
        assertThat(g.contents).containsExactly(contents[0], contents[1], contents[3], contents[4])
    }

    @Test
    fun objectCacheTest() {
        val g1 = createObject(SGalaxy::class)
        val g2 = g1.oid.load(SGalaxy::class)
        assertThat(g1).isSameAs(g2)
    }
}