load(
    "//:kotlin.bzl",
    "kotlin_js_lib",
    "kotlin_js_binary",
)

package(default_visibility = ["//visibility:public"])

kotlin_js_binary(
    name = "client_js",
    srcs = ["main.kt"],
    deps = [
        "//src/interfaces:interfaces_jsjar",
        "//src/model:model_jsjar",
        "//src/runtime/js:runtime_jsjar",
        "//src/runtime/shared:shared_jsjar",
        "//src/utils:utils_jsjar",
        "@org_jetbrains_kotlin_kotin_stdlib_js//jar",
    ],
)
