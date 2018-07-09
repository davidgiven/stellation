load(
    "//:kotlin.bzl",
    "kotlin_konan_lib",
    "kotlin_konan_cinterop",
)

package(default_visibility = ["//visibility:public"])

kotlin_konan_cinterop(
    name = "sqlite3_klib",
    src = "sqlite3.def",
    libname = "sqlite3",
)

kotlin_konan_lib(
    name = "runtime_klib",
    srcs = glob(["*.kt"]),
    deps = [
        ":sqlite3_klib",
        "//src/interfaces:interfaces_klib",
        "//src/utils:utils_klib",
    ],
)
