load("//:kotlin.bzl", "kotlin_jvm_test")
load("//:variables.bzl", "JVMTESTLIBS")

package(default_visibility = ["//visibility:public"])

kotlin_jvm_test(
    name = "RuntimeSharedTest",
    size = "small",
    srcs = glob(["*.kt"]),
    libs = JVMTESTLIBS,
    test_class = "runtime.shared.AllTestsSuite",
    deps = [
        "//src/interfaces",
        "//src/model",
        "//src/runtime/jvm",
        "//src/runtime/shared",
        "//src/utils",
    ],
)
