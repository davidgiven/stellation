load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_js_lib",
    "kotlin_konan_lib",
)

package(default_visibility = ["//visibility:public"])

SRCS = glob(["*.kt"])

kotlin_jvm_lib(
    name = "commands_jar",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/model:model_jar",
        "//src/utils:utils_jar",
    ],
)

kotlin_konan_lib(
    name = "commands_klib",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_klib",
        "//src/model:model_klib",
        "//src/utils:utils_klib",
    ],
)

kotlin_js_lib(
    name = "commands_jsjar",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_jsjar",
        "//src/model:model_jsjar",
        "//src/utils:utils_jsjar",
    ],
)
