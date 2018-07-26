load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_js_lib",
    "kotlin_konan_lib",
)

package(default_visibility = ["//visibility:public"])

SRCS = glob(
    ["*.kt"],
    exclude = ["hack_*.kt"],
)

kotlin_jvm_lib(
    name = "interfaces_jar",
    srcs = SRCS + glob(["hack_jvm_*.kt"]),
    deps = ["//src/utils:utils_jar"],
)

kotlin_js_lib(
    name = "interfaces_jsjar",
    srcs = SRCS + glob(["hack_js_*.kt"]),
    deps = ["//src/utils:utils_jsjar"],
)

kotlin_konan_lib(
    name = "interfaces_klib",
    srcs = SRCS + glob(["hack_konan_*.kt"]),
    deps = ["//src/utils:utils_klib"],
)
