load("//:kotlin.bzl", "kotlin_jvm_test")
load("//:variables.bzl", "JVMTESTLIBS")

package(default_visibility = ["//visibility:public"])

kotlin_jvm_test(
    name = "UtilsTest",
    size = "small",
    srcs = glob(["*.kt"]),
    libs = JVMTESTLIBS,
    test_class = "utils.AllTestsSuite",
    deps = [
        "//src/interfaces",
        "//src/runtime/jvm",
        "//src/utils",
    ],
)
