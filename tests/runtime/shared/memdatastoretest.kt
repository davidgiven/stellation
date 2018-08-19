package runtime.shared

import interfaces.IClock
import interfaces.IDatastore
import utils.bind
import utils.resetBindingsForTest
import kotlin.test.BeforeTest

class InMemoryDatastoreTest : AbstractDatastoreTest() {
    @BeforeTest
    fun setup() {
        resetBindingsForTest()
        bind<IClock>(ServerClock())
        bind<IDatastore>(InMemoryDatastore())
        datastore.initialiseDatabase()
        createProperties()
    }
}
