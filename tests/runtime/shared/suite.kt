package runtime.shared

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        InMemoryDatastoreTest::class,
        SqlDatastoreTest::class,
        ServerMessageTest::class,
        BCryptTest::class)
class AllTestsSuite
