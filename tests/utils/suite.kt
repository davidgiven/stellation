package utils

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        ArgifierTest::class,
        GetoptTest::class,
        InjectomaticTest::class,
        MessageTest::class,
        MiscTest::class,
        ThreadTest::class)
class AllTestsSuite
