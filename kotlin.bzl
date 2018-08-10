def kotlin_jvm_lib(name, srcs=[], deps=[]):
  native.filegroup(
      name = name,
      srcs = srcs
  )

def kotlin_jvm_binary(name, srcs=[], deps=[], libs=[], **kwargs):
  kotlin_jvm_lib(
      name = name + "_main",
      srcs = srcs
  )

  cmd = ["/home/dg/src/kotlinc/bin/kotlinc-jvm",
         "-Xcoroutines=enable",
         "-d $@",
         "$(location {}_main)".format(name)]

  for dep in deps:
      cmd += ["$(locations {})".format(dep)]

  classpath = ":".join(["$(location {})".format(jar) for jar in libs])
  if classpath != "":
    cmd += ["-cp {}".format(classpath)]

  native.genrule(
    name = name + "_jar",
    message = "Building JVM {0}".format(name),
    srcs = srcs + deps + libs + [name + "_main"],
    outs = [name + ".kotlin.jar"],
    cmd = " ".join(cmd)
  )

  jars = " ".join(["$(location {})".format(dep) for dep in deps])

  native.java_binary(
      name = name,
      message = "Linking JVM {0}".format(name),
      runtime_deps = [":{}_jar".format(name)] + libs,
      **kwargs
  )

def kotlin_jvm_test(name, srcs=[], deps=[], libs=[], **kwargs):
  jars = " ".join(["$(location {})".format(dep) for dep in deps])

  kotlin_jvm_lib(
      name = name + "_main_jar",
      srcs = srcs,
      deps = deps + libs
  )

  native.java_import(
      name = name + "_jars",
      jars = deps + [":{}_main_jar".format(name)]
  )

  native.java_test(
      name = name,
      runtime_deps = [":{}_jars".format(name)] + libs,
      **kwargs
  )

def kotlin_js_lib(name, srcs=[], deps=[], main=False):
  native.filegroup(
      name = name,
      srcs = srcs
  )

def kotlin_js_binary(name, srcs=[], deps=[]):
  native.genrule(
    name = "{}_compiled".format(name),
    message = "Building JS {0}".format(name),
    srcs = srcs,
    outs = ["{0}.main.js".format(name)],
    cmd = " ".join([
        "/home/dg/src/kotlinc/bin/kotlinc-js",
        "-Xcoroutines=enable",
        "-module-kind commonjs",
        "-output $@",
        "-meta-info",
        "$(SRCS)"
    ])
  )

  allDeps = deps + [":{0}.main.js".format(name)]
  native.genrule(
      name = "{}_modules".format(name),
      message = "DCE JS {}".format(name),
      srcs = allDeps,
      outs = [name + ".tar"],
      output_to_bindir = 1,
      cmd = " && ".join([
          " ".join([
              "/home/dg/src/kotlinc/bin/kotlin-dce-js",
              "-output-dir $@.files/node_modules",
          ] + ["$(locations {})".format(d) for d in allDeps]
          ),
		  "tar cf $@ -C $@.files/node_modules ."
      ])
  )

  native.genrule(
      name = name,
      message = "Linking JS {}".format(name),
      srcs = [":{}_modules".format(name)],
      outs = [name + ".js"],
      output_to_bindir = 1,
      cmd = " && ".join([
	  	  "mkdir -p $@.files/node_modules",
	      "tar xf $< -C $@.files/node_modules",
          "(cd $@.files && " + " ".join([
              "webpack",
              "--display none",
              "{}.main.js".format(name),
              "out.js",
          ]) + ")",
          "cp $@.files/out.js $@"
      ])
  )

def kotlin_konan_lib(name, srcs=[], deps=[]):
  native.filegroup(
      name = name,
      srcs = srcs
  )

def kotlin_konan_binary(name, srcs=[], deps=[], libs=[], main="main"):
  kotlin_konan_lib(
      name = name + "_main",
      srcs = srcs
  )

  cmd = ["/home/dg/src/kotlin-native-linux-0.8.1/bin/kotlinc-native",
         "-produce program",
         "--purge_user_libs",
         "-entry {}".format(main),
         "-g",
         "--disable devirtualization",
         "-output $@",
         "$(location {}_main)".format(name)]

  for dep in deps:
    cmd += ["$(locations {})".format(dep)]
  for lib in libs:
    cmd += ["-repo $$(dirname $(location {}))".format(lib)]
    cmd += ["-library $(location {})".format(lib)]

  native.genrule(
    name = name,
    message = "Compiling Konan {0}".format(name),
    srcs = srcs + libs + deps + [":{}_main".format(name)],
    outs = [name + ".kexe"],
    cmd = " ".join(cmd),
    output_to_bindir = 1,
  )

def kotlin_konan_cinterop(name, src, libname):
  native.genrule(
    name = name,
    message = "Generating Konan cinterop {}".format(name),
    srcs = [src],
    outs = [libname + ".klib"],
    cmd = " ".join([
        "/home/dg/src/kotlin-native-linux-0.8.1/bin/cinterop",
        "-def $<",
        "-o $@",
    ])
  )
