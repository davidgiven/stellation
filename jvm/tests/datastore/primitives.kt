package datastore

import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import shared.DatabaseTypeMismatchException
import shared.ObjectNotVisibleException
import shared.SFactory
import shared.SGalaxy
import shared.SModule
import shared.SStar
import shared.SUniverse
import shared.createObject
import shared.load
import shared.loadObject
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

        val s2 = s1.oid.load(SStar::class)
        assertThat(s2.name).isEqualTo("Foo")
        assertThat(s2.brightness).isEqualTo(7.6)
        assertThat(s2.asteroidsM).isEqualTo(42)
    }

    @Test
    fun objectEqualityTest() {
        val s1 = createObject(SStar::class)
        val s2 = s1.oid.load(SStar::class)

        assertThat(s1).isEqualTo(s2)
    }

    @Test
    fun objectDoesNotExistTest() {
        thrown.expect(ObjectNotVisibleException::class.java)
        loadObject(42, SStar::class)
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
        var m = f.oid.load(SModule::class)

        assertThat(f.kind).isEqualTo(m.kind)
    }

    @Test
    fun upcastTest() {
        thrown.expect(DatabaseTypeMismatchException::class.java)

        var m = createObject(SModule::class)
        m.oid.load(SFactory::class)
    }

    @Test
    fun addItemsToSetTest() {
        var g = createObject(SGalaxy::class)
        var stars = Array(5) { createObject(SStar::class) }
        stars.forEach { g.stars += it }

        assertThat(g.stars).containsExactlyElementsIn(stars)
    }

    @Test
    fun removeItemsFromSetTest() {
        var g = createObject(SGalaxy::class)
        var stars = Array(5) { createObject(SStar::class) }
        stars.forEach { g.stars += it }

        g.stars -= stars[2]

        assertThat(g.stars).containsExactly(stars[0], stars[1], stars[3], stars[4])
    }

    @Test
    fun getItemFromSetTest() {
        var g = createObject(SGalaxy::class)
        var stars = List(5) { createObject(SStar::class) }
        stars.forEach { g.stars += it }

        while (true) {
            var s = g.stars.getOne()
            s ?: break

            assertThat(s).isIn(stars)
            g.stars -= s
            stars -= s
        }

        assertThat(stars).isEmpty()
    }
}