package runtime.shared

import interfaces.IContext
import interfaces.context
import kotlin.test.BeforeTest

class InMemoryDatastoreTest : AbstractDatastoreTest() {
    @BeforeTest
    fun setup() {
        context = object : IContext() {
            override val datastore = InMemoryDatastore()
        }

        datastore.initialiseDatabase()
    }
}
