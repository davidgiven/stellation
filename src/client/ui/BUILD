load("//:kotlin.bzl", "kotlin_js_lib")

package(default_visibility = ["//visibility:public"])

kotlin_js_lib(
    name = "client_ui_jsjar",
    srcs = glob(["*.kt"]),
    deps = [
        "//src/interfaces:interfaces_jsjar",
        "//src/model:model_jsjar",
        "//src/runtime/js:runtime_jsjar",
        "//src/runtime/shared:shared_jsjar",
        "//src/ui:ui_jsjar",
        "//src/utils:utils_jsjar",
        "@org_jetbrains_kotlin_kotin_stdlib_js//jar",
    ],
)
