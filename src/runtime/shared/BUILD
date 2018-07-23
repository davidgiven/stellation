load(
    "//:kotlin.bzl",
    "kotlin_konan_lib",
    "kotlin_jvm_lib",
    "kotlin_js_lib",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_lib(
    name = "shared_jar",
    srcs = glob(["*.kt"]),
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/model:model_jar",
        "//src/utils:utils_jar",
    ],
)

kotlin_konan_lib(
    name = "shared_klib",
    srcs = glob(["*.kt"]),
    deps = [
        "//src/interfaces:interfaces_klib",
        "//src/model:model_klib",
        "//src/utils:utils_klib",
    ],
)

kotlin_js_lib(
    name = "shared_jsjar",
    srcs = glob(["*.kt"]),
    deps = [
        "//src/interfaces:interfaces_jsjar",
        "//src/model:model_jsjar",
        "//src/utils:utils_jsjar",
    ],
)
