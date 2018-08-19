package model

import interfaces.IClock
import interfaces.IDatastore
import runtime.shared.ServerClock
import runtime.shared.InMemoryDatastore
import utils.bind
import utils.resetBindingsForTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class InMemoryObjectsTest : AbstractObjectsTest() {
    @BeforeTest
    fun setup() {
        resetBindingsForTest()
        bind<IDatastore>(InMemoryDatastore())
        bind<IClock>(ServerClock())
        bind(Model())

        datastore.initialiseDatabase()
    }

    @AfterTest
    fun teardown() {
    }
}
