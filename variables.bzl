JVMLIBS = [
    "@org_jetbrains_kotlin_kotin_reflect//jar",
    "@org_jetbrains_kotlin_kotin_stdlib//jar",
    "@org_xerial_sqlite_jdbc//jar",
    "@org_jetbrains_kotlinx_kotlinx_coroutines_core//jar",
]

JVMTESTLIBS = JVMLIBS + [
    "@junit_junit//jar",
    "@org_jetbrains_kotlin_kotin_test//jar",
    "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
    "@org_jetbrains_kotlin_kotin_test_junit//jar",
]
