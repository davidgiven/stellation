load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_konan_lib",
)

package(default_visibility = ["//visibility:public"])

SRCS = glob(["*.kt"])

kotlin_jvm_lib(
    name = "server_jar",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/model:model_jar",
        "//src/runtime/jvmhacks:hacks_jar",
        "//src/runtime/shared:shared_jar",
        "//src/utils:utils_jar",
    ],
)

kotlin_konan_lib(
    name = "server_klib",
    srcs = SRCS,
    deps = [
        "//src/interfaces:interfaces_klib",
        "//src/model:model_klib",
        "//src/runtime/konanhacks:hacks_klib",
        "//src/runtime/shared:shared_klib",
        "//src/utils:utils_klib",
    ],
)
