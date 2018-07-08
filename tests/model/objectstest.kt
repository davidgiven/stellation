package model

import datastore.IDatabase
import interfaces.IDatastore
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

    @BeforeTest
    fun setup() {
        bind<IDatabase>(JvmDatabase())
        bind<IDatastore>(SqlDatastore(database))

        database.openDatabase(":memory:")
        datastore.initialiseDatabase()
    }

    @AfterTest
    fun teardown() {
        database.closeDatabase()
    }

    @Test
    fun emptyTest() {
    }
}
