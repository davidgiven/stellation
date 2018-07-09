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
    name = "SqlObjectsTest",
    size = "small",
    srcs = [
        "abstractobjectstest.kt",
        "sqlobjectstest.kt",
    ],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_reflect//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_xerial_sqlite_jdbc//jar",
    ],
    test_class = "model.SqlObjectsTest",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/model:model_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/runtime/shared:shared_jar",
        "//src/utils:utils_jar",
    ],
)

kotlin_jvm_test(
    name = "ThingTest",
    size = "small",
    srcs = [
        "thingtest.kt",
    ],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_reflect//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_xerial_sqlite_jdbc//jar",
    ],
    test_class = "model.ThingTest",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/model:model_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/runtime/shared:shared_jar",
        "//src/utils:utils_jar",
    ],
)

kotlin_jvm_test(
    name = "InMemoryObjectsTest",
    size = "small",
    srcs = [
        "abstractobjectstest.kt",
        "memobjectstest.kt",
    ],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_reflect//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
    ],
    test_class = "model.InMemoryObjectsTest",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/model:model_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/runtime/shared:shared_jar",
        "//src/utils:utils_jar",
    ],
)

kotlin_jvm_test(
    name = "TimersTest",
    size = "small",
    srcs = [
        "timerstest.kt",
    ],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_reflect//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_xerial_sqlite_jdbc//jar",
    ],
    test_class = "model.TimersTest",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/model:model_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/runtime/shared:shared_jar",
        "//src/utils:utils_jar",
    ],
)
