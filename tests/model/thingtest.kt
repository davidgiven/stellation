package model

import interfaces.IClock
import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.withSqlTransaction
import runtime.jvm.JvmDatabase
import runtime.shared.Clock
import runtime.shared.SqlDatastore
import utils.Fault
import utils.FaultDomain.INVALID_ARGUMENT
import utils.bind
import utils.inject
import utils.resetBindingsForTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ThingTest {
    private val database get() = inject<IDatabase>()
    private val datastore get() = inject<IDatastore>()
    private val model get() = inject<Model>()

    @BeforeTest
    fun setup() {
        resetBindingsForTest()
        bind<IClock>(Clock())
        bind<IDatabase>(JvmDatabase())
        bind<IDatastore>(SqlDatastore())
        bind(Model())

        database.openDatabase(":memory:")
        datastore.initialiseDatabase()
        database.withSqlTransaction { model.initialiseProperties() }
        database.executeSql("BEGIN")
    }

    @AfterTest
    fun teardown() {
        database.executeSql("COMMIT")
        database.closeDatabase()
    }

    @Test
    fun moveToTest() {
        val g = model.createObject(SGalaxy::class)
        val s1 = model.createObject(SStar::class).moveTo(g)
        assertEquals(g, s1.location)
        assertEquals(setOf(s1), g.contents.getAll())

        val s2 = model.createObject(SStar::class).moveTo(g)
        assertEquals(g, s2.location)
        assertEquals(setOf(s1, s2), g.contents.getAll())

        s1.remove()
        assertEquals(null, s1.location)
        assertEquals(setOf(s2), g.contents.getAll())

        s2.remove()
        assertEquals(null, s2.location)
        assertEquals(emptySet(), g.contents.getAll())
    }

    @Test
    fun invalidMoveTest() {
        val g = model.createObject(SGalaxy::class)
        val s = model.createObject(SStar::class).moveTo(g)

        try {
            g.moveTo(s)
        } catch (f: Fault) {
            assertEquals(INVALID_ARGUMENT, f.domain)
            assertContains("contained by itself", f.detail)
        }
    }

    @Test
    fun multipleRemoveTest() {
        val g = model.createObject(SGalaxy::class)
        val s = model.createObject(SStar::class).moveTo(g)
        s.remove()
        s.remove()
        assertEquals(null, s.location)
        assertEquals(emptySet(), g.contents.getAll())
    }

    @Test
    fun removeFromNowhereTest() {
        model.createObject(SGalaxy::class).remove()
    }

    @Test
    fun findChildTest() {
        val g = model.createObject(SGalaxy::class)
        val s = model.createObject(SStar::class).moveTo(g)

        assertEquals(s, g.findChild<SStar>())
        assertNull(g.findChild<SPlayer>())
    }

    @Test
    fun hierarchyTest() {
        val g = model.createObject(SGalaxy::class)
        val s1 = model.createObject(SStar::class).moveTo(g)
        val s2 = model.createObject(SStar::class).moveTo(g)
        val sh = model.createObject(SShip::class).moveTo(s1)

        assertEquals(setOf(g, s1, s2, sh), g.calculateHierarchicalContents())
    }

    private fun assertContains(needle: String, haystack: String) {
        assertTrue(needle in haystack)
    }

}
