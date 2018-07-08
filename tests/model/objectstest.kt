package model

import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.withSqlTransaction
import runtime.jvm.JvmDatabase
import runtime.shared.SqlDatastore
import utils.bind
import utils.get
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ObjectsTest {
    private val database get() = get<IDatabase>()
    private val datastore get() = get<IDatastore>()
    private val model get() = get<Model>()

    @BeforeTest
    fun setup() {
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
    fun objectCreationTest() {
        val universe = model.createObject(SUniverse::class)
        assertEquals(1, universe.oid)
        assertEquals("SUniverse", universe.kind)
    }
}
