load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_jvm_test",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_test(
    name = "FrameTest",
    size = "small",
    srcs = ["frame.kt"],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
    ],
    test_class = "model.FrameTest",
    deps = [
        "//src/model:model_jar",
    ],
)

kotlin_jvm_test(
    name = "ObjectsTest",
    size = "small",
    srcs = ["objectstest.kt"],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_reflect//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_xerial_sqlite_jdbc//jar",
    ],
    test_class = "model.ObjectsTest",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/model:model_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/runtime/shared:shared_jar",
        "//src/utils:utils_jar",
    ],
)
