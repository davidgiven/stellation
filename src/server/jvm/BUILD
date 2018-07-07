load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_jvm_binary",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_binary(
    name = "server_jvm",
    srcs = ["main.kt"],
    libs = [
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
        "@org_xerial_sqlite_jdbc//jar",
    ],
    main_class = "server.jvm.MainKt",
    deps = [
        "//src/interfaces:interfaces_jar",
        "//src/model:model_jar",
        "//src/runtime/jvm:runtime_jar",
        "//src/server:server_jar",
        "//src/utils:utils_jar",
    ],
)
