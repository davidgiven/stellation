load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_jvm_test",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_test(
    name = "DatastoreTest",
    size = "small",
    srcs = ["datastore.kt"],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_xerial_sqlite_jdbc//jar",
    ],
    test_class = "runtime.jvm.DatastoreTest",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/runtime/shared:shared_jar",
    ],
)
