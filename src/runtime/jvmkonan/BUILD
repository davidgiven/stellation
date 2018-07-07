load(
    "//:kotlin.bzl",
    "kotlin_konan_lib",
    "kotlin_jvm_lib",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_lib(
    name = "jvmkonan_jar",
    srcs = glob(["*.kt"]),
    deps = ["//src/interfaces:interfaces_jar"],
)

kotlin_konan_lib(
    name = "jvmkonan_klib",
    srcs = glob(["*.kt"]),
    deps = ["//src/interfaces:interfaces_klib"],
)
