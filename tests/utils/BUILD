load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_jvm_test",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_test(
    name = "UtilsTest",
    size = "small",
    srcs = glob(["*.kt"]),
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
        "@org_jetbrains_kotlin_kotin_reflect//jar",
    ],
    test_class = "utils.AllTestsSuite",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/utils:utils_jar",
    ],
)
