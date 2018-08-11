load("@bazel_tools//tools/build_defs/pkg:pkg.bzl", "pkg_tar")
load("//:kotlin.bzl", "kotlin_js_binary")

package(default_visibility = ["//visibility:public"])

filegroup(
    name = "client",
    srcs = glob(["*.kt"]),
)

kotlin_js_binary(
    name = "client_js",
    srcs = [
        ":client",
        "//src/client/ui",
        "//src/commands",
        "//src/interfaces",
        "//src/model",
        "//src/runtime/js",
        "//src/runtime/shared",
        "//src/ui",
        "//src/utils",
    ],
    deps = [
        "@org_jetbrains_kotlin_kotin_stdlib_js//jar",
        "@org_jetbrains_kotlinx_atomicfu_js//jar",
        "@org_jetbrains_kotlinx_kotlinx_coroutines_core_js//jar",
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
        #        "--compress warnings=false",
        "--beautify",
        #        "--mangle",
        #        "--mangle-props=2",
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

genrule(
    name = "client_html",
    srcs = [
        "client.html",
        "library.svg",
        ":sass_rules",
    ],
    outs = ["index.html"],
    cmd = " ".join([
        "gpp",
        "-H",
        "-I $$(dirname $(location :sass_rules))",
        "-I $$(dirname $(location library.svg))",
        "-o $@",
        "$(location client.html)",
    ]),
)

pkg_tar(
    name = "client_pkg",
    srcs = glob(["resources/**"]) + [
        ":uglified_client_js",
        ":client_html",
    ],
    extension = "tar.gz",
)
