load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_jvm_test",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_test(
    name = "SqlDatastoreTest",
    size = "small",
    srcs = [
        "abstractdatastoretest.kt",
        "sqldatastoretest.kt",
    ],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_xerial_sqlite_jdbc//jar",
    ],
    test_class = "runtime.shared.SqlDatastoreTest",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/runtime/shared:shared_jar",
    ],
)

kotlin_jvm_test(
    name = "InMemoryDatastoreTest",
    size = "small",
    srcs = [
        "abstractdatastoretest.kt",
        "memdatastoretest.kt",
    ],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_xerial_sqlite_jdbc//jar",
    ],
    test_class = "runtime.shared.InMemoryDatastoreTest",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/runtime/shared:shared_jar",
    ],
)
