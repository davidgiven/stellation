load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_jvm_test",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_test(
    name = "FormDecoderTest",
    size = "small",
    srcs = ["formdecodertest.kt"],
    libs = [
        "@junit_junit//jar",
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_jetbrains_kotlin_kotin_test//jar",
        "@org_jetbrains_kotlin_kotin_test_annotations_common//jar",
        "@org_jetbrains_kotlin_kotin_test_junit//jar",
    ],
    test_class = "server.FormDecoderTest",
    deps = [
        "//src/runtime/jvmhacks:hacks_jar",
        "//src/server:server_jar",
    ],
)
