load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
    "kotlin_js_lib",
    "kotlin_konan_lib",
    "kotlin_jvm_binary",
    "kotlin_js_binary",
    "kotlin_konan_binary",
)

kotlin_jvm_lib(
    name = "util_jar",
    srcs = glob(["src/util.kt"]),
)

kotlin_jvm_lib(
    name = "main_jar",
    srcs = glob(["src/main.kt"]),
    deps = [":util_jar"],
)

kotlin_jvm_binary(
    name = "jvm_bin",
    libs = [
        "@org_jetbrains_kotlin_kotin_stdlib//jar",
    ],
    main_class = "MainKt",
    deps = [
        ":main_jar",
        ":util_jar",
    ],
)

kotlin_js_lib(
    name = "util_jsjar",
    srcs = glob(["src/util.kt"]),
)

kotlin_js_lib(
    name = "main_jsjar",
    srcs = ["src/main.kt"],
    main = True,
    deps = [":util_jsjar"],
)

kotlin_js_binary(
    name = "js_bin",
    deps = [
        ":main_jsjar",
        ":util_jsjar",
        "@org_jetbrains_kotlin_kotin_stdlib_js//jar",
    ],
)

kotlin_konan_lib(
    name = "util_klib",
    srcs = glob(["src/util.kt"]),
)

kotlin_konan_binary(
    name = "prog_kexe",
    srcs = glob(["src/main.kt"]),
    deps = [
        ":util_klib",
    ],
)
