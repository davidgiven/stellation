load("//:kotlin.bzl", "kotlin_konan_cinterop")

package(default_visibility = ["//visibility:public"])

kotlin_konan_cinterop(
    name = "sqlite3_klib",
    src = "sqlite3.def",
    libname = "sqlite3",
)

filegroup(
    name = "konan",
    srcs = glob(["*.kt"]),
)
