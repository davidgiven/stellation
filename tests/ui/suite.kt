package ui

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        AbstractFormTest::class,
        CoreUiTest::class)
class AllTestsSuite
