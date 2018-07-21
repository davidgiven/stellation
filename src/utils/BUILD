load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_js_lib",
    "kotlin_konan_lib",
)

package(default_visibility = ["//visibility:public"])

SRCS = glob(["*.kt"])

kotlin_jvm_lib(
    name = "utils_jar",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_jar",
    ],
)

kotlin_konan_lib(
    name = "utils_klib",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_klib",
    ],
)

kotlin_js_lib(
    name = "utils_jsjar",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_jsjar",
    ],
)
