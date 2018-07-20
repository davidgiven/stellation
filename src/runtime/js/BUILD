load(
    "//:kotlin.bzl",
    "kotlin_js_lib",
)

package(default_visibility = ["//visibility:public"])

kotlin_js_lib(
    name = "runtime_jsjar",
    srcs = glob(["*.kt"]),
    deps = [
        "//src/interfaces:interfaces_jsjar",
        "//src/runtime/shared:shared_jsjar",
        "//src/ui:ui_jsjar",
        "//src/utils:utils_jsjar",
        "@org_jetbrains_kotlinx_kotlinx_coroutines_core_js//jar",
    ],
)
