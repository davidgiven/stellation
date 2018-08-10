load("//:kotlin.bzl", "kotlin_konan_binary")

package(default_visibility = ["//visibility:public"])

kotlin_konan_binary(
    name = "server_kexe",
    srcs = ["main.kt"],
    libs = [
        "//src/runtime/konan:sqlite3_klib",
    ],
    main = "server.konan.main",
    deps = [
        "//src/commands",
        "//src/interfaces",
        "//src/model",
        "//src/runtime/konan",
        "//src/runtime/shared",
        "//src/server",
        "//src/utils",
    ],
)
