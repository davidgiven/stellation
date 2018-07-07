load(
    "//:kotlin.bzl",
    "kotlin_konan_lib",
)

package(default_visibility = ["//visibility:public"])

kotlin_konan_lib(
    name = "runtime_klib",
    srcs = glob(["*.kt"]),
    deps = ["//src/interfaces:interfaces_klib"],
)
