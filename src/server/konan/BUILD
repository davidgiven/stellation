load(
    "//:kotlin.bzl",
    "kotlin_js_binary",
    "kotlin_js_lib",
    "kotlin_jvm_binary",
    "kotlin_jvm_lib",
    "kotlin_konan_binary",
    "kotlin_konan_lib",
)

package(default_visibility = ["//visibility:public"])

kotlin_konan_binary(
    name = "server_kexe",
    srcs = ["main.kt"],
    libs = [
        "//src/runtime/konan:sqlite3_klib",
    ],
    main = "server.konan.main",
    deps = [
        "//src/commands:commands_klib",
        "//src/interfaces:interfaces_klib",
        "//src/model:model_klib",
        "//src/runtime/konan:runtime_klib",
        "//src/runtime/shared:shared_klib",
        "//src/server:server_klib",
        "//src/utils:utils_klib",
    ],
)
