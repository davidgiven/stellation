load("//:kotlin.bzl", "kotlin_jvm_binary")
load("//:variables.bzl", "JVMLIBS")

package(default_visibility = ["//visibility:public"])

kotlin_jvm_binary(
    name = "server_jvm",
    srcs = ["main.kt"],
    libs = JVMLIBS,
    main_class = "server.jvm.MainKt",
    deps = [
        "//src/commands",
        "//src/interfaces",
        "//src/model",
        "//src/runtime/jvm",
        "//src/runtime/shared",
        "//src/server",
        "//src/utils",
    ],
)
