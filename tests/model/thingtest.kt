package model

import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.withSqlTransaction
import kotlin.test.Test
import runtime.jvm.JvmDatabase
import runtime.shared.SqlDatastore
import utils.bind
import utils.get
import utils.resetBindingsForTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class ThingTest {
    private val database get() = get<IDatabase>()
    private val datastore get() = get<IDatastore>()
    private val model get() = get<Model>()

    @BeforeTest
    fun setup() {
        resetBindingsForTest()
        bind<IDatabase>(JvmDatabase())
        bind<IDatastore>(SqlDatastore(database))
        bind(Model(datastore))

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
        assertEquals(listOf(s1), g.contents.getAll())

        val s2 = model.createObject(SStar::class).moveTo(g)
        assertEquals(g, s2.location)
        assertEquals(listOf(s1, s2), g.contents.getAll())

        s1.remove()
        assertEquals(null, s1.location)
        assertEquals(listOf(s2), g.contents.getAll())

        s2.remove()
        assertEquals(null, s2.location)
        assertEquals(emptyList(), g.contents.getAll())
    }

    @Test
    fun multipleRemoveTest() {
        val g = model.createObject(SGalaxy::class)
        val s = model.createObject(SStar::class).moveTo(g)
        s.remove()
        s.remove()
        assertEquals(null, s.location)
        assertEquals(emptyList(), g.contents.getAll())
    }

    @Test
    fun removeFromNowhereTest() {
        model.createObject(SGalaxy::class).remove()
    }
}
