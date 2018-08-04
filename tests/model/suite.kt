package model

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        FrameTest::class,
        InMemoryObjectsTest::class,
        SqlObjectsTest::class,
        ThingTest::class,
        TimersTest::class,
        VisibilityTest::class,
        SyncTest::class)
class AllTestsSuite
