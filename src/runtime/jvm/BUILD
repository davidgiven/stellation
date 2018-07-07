load(
    "//:kotlin.bzl",
    "kotlin_jvm_lib",
)

package(default_visibility = ["//visibility:public"])

kotlin_jvm_lib(
    name = "runtime_jar",
    srcs = glob(["*.kt"]),
    deps = [
        "//src/interfaces:interfaces_jar",
        "@org_xerial_sqlite_jdbc//jar",
    ],
)
