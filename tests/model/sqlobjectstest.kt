package model

import interfaces.IClock
import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.withSqlTransaction
import runtime.jvm.JvmDatabase
import runtime.shared.Clock
import runtime.shared.SqlDatastore
import utils.bind
import utils.resetBindingsForTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class SqlObjectsTest : AbstractObjectsTest() {
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
}
