load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_js_lib",
    "kotlin_konan_lib",
    "kotlin_jvm_binary",
    "kotlin_js_binary",
    "kotlin_konan_binary",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_binary(
    name = "server_jvm",
    srcs = ["main.kt"],
    libs = [
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
    ],
    main_class = "MainKt",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/runtime/jvm:runtime_jar",
    ],
)

kotlin_konan_binary(
    name = "server_kexe",
    srcs = ["main.kt"],
    main = "server.main",
    deps = [
        "//src/interfaces:interfaces_klib",
        "//src/runtime/konan:runtime_klib",
    ],
)
