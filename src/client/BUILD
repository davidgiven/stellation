load("@bazel_tools//tools/build_defs/pkg:pkg.bzl", "pkg_tar")
load("//:kotlin.bzl", "kotlin_js_lib", "kotlin_js_binary")

package(default_visibility = ["//visibility:public"])

kotlin_js_binary(
    name = "client_js",
    srcs = ["main.kt"],
    deps = [
        "//src/interfaces:interfaces_jsjar",
        "//src/model:model_jsjar",
        "//src/runtime/js:runtime_jsjar",
        "//src/runtime/shared:shared_jsjar",
        "//src/utils:utils_jsjar",
        "@org_jetbrains_kotlin_kotin_stdlib_js//jar",
    ],
)

genrule(
    name = "uglified_client_js",
    srcs = [":client_js"],
    outs = ["stellation.js"],
    cmd = " ".join([
        "uglifyjs",
        "$<",
        "-o $@",
        "--compress warnings=false",
        "--beautify",
        #"--mangle",
        #"--mangle-props=2",
    ]),
)

genrule(
    name = "sass_rules",
    srcs = ["style.scss"],
    outs = ["style.css"],
    cmd = " ".join([
        "sassc",
        "--style=nested",
        "$<",
        "$@",
    ]),
)

pkg_tar(
    name = "client_pkg",
    srcs = glob(["resources/**"]) + [
        ":uglified_client_js",
        ":sass_rules",
    ],
    extension = "tar.gz",
)
