load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_jvm_test",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_test(
    name = "GetoptTest",
    size = "small",
    srcs = ["getopt.kt"],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
    ],
    test_class = "utils.GetoptTest",
    deps = [
        "//src/utils:utils_jar",
    ],
)

kotlin_jvm_test(
    name = "InjectomaticTest",
    size = "small",
    srcs = ["injectomatictest.kt"],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_jetbrains_kotlin_kotin_reflect//jar",
    ],
    test_class = "utils.InjectomaticTest",
    deps = [
        "//src/utils:utils_jar",
    ],
)

kotlin_jvm_test(
    name = "ThreadTest",
    size = "small",
    srcs = ["threadtest.kt"],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_jetbrains_kotlin_kotin_reflect//jar",
    ],
    test_class = "utils.ThreadTest",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/utils:utils_jar",
    ],
)

kotlin_jvm_test(
    name = "MiscTest",
    size = "small",
    srcs = ["misctest.kt"],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_jetbrains_kotlin_kotin_reflect//jar",
    ],
    test_class = "utils.MiscTest",
    deps = [
        "//src/utils:utils_jar",
    ],
)
