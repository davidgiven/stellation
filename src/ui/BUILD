load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_js_lib",
    "kotlin_konan_lib",
)

package(default_visibility = ["//visibility:public"])

SRCS = glob(["*.kt"])

kotlin_jvm_lib(
    name = "ui_jar",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/utils:utils_jar",
        "@org_jetbrains_kotlinx_kotlinx_coroutines_core//jar",
    ],
)

kotlin_js_lib(
    name = "ui_jsjar",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_jsjar",
        "//src/utils:utils_jsjar",
        "@org_jetbrains_kotlinx_kotlinx_coroutines_core_js//jar",
    ],
)
