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
    ],
)
