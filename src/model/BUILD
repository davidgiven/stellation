load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_js_lib",
    "kotlin_konan_lib",
)

package(default_visibility = ["//visibility:public"])

SRCS = glob(["*.kt"])

kotlin_jvm_lib(
    name = "model_jar",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/utils:utils_jar",
    ],
)

kotlin_konan_lib(
    name = "model_klib",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_klib",
        "//src/utils:utils_klib",
    ],
)

kotlin_js_lib(
    name = "model_jsjar",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_jsjar",
        "//src/utils:utils_jsjar",
    ],
)
