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
    }

    @AfterTest
    fun teardown() {
        database.closeDatabase()
    }

    @Test
    fun objectCreationTest() {
        val universe = model.createObject(SUniverse::class)
    }
}
