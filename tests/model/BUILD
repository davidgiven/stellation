load("//:kotlin.bzl", "kotlin_jvm_test")
load("//:variables.bzl", "JVMTESTLIBS")

package(default_visibility = ["//visibility:public"])

kotlin_jvm_test(
    name = "ModelTests",
    size = "small",
    srcs = glob(["*.kt"]),
    libs = JVMTESTLIBS,
    test_class = "model.AllTestsSuite",
    deps = [
        "//src/interfaces",
        "//src/model",
        "//src/runtime/jvm",
        "//src/runtime/shared",
        "//src/utils",
    ],
)
