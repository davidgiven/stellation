def kotlin_jvm_lib(name, srcs=[], deps=[]):
  cmd = ["/home/dg/src/kotlinc/bin/kotlinc-jvm",
         "-Xcoroutines=enable",
         "-d $@"]
  for f in srcs:
    cmd += ["$(location {})".format(f)]

  classpath = ":".join(["$(location {})".format(jar) for jar in deps])
  if classpath != "":
    cmd += ["-cp {}".format(classpath)]

  native.genrule(
    name = name,
    message = "Building JVM {0}".format(name),
    srcs = srcs + deps,
    outs = [name + ".jar"],
    cmd = " ".join(cmd)
  )

def kotlin_jvm_binary(name, srcs=[], deps=[], libs=[], **kwargs):
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

  native.java_binary(
      name = name,
      runtime_deps = [":{}_jars".format(name)] + libs,
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
  cmd = ["/home/dg/src/kotlinc/bin/kotlinc-js",
         "-Xcoroutines=enable",
         "-module-kind commonjs",
         "-output {0}/files/{0}.js".format(name),
         "-meta-info"]
  if not main:
    cmd += ["-main noCall"]
  cmd += ["$(location {})".format(src) for src in srcs]

  libraries = ":".join(["$(location {})".format(jar) for jar in deps])
  if libraries:
    cmd += ["-libraries {}".format(libraries)]

  native.genrule(
    name = name,
    message = "Building JS {0}".format(name),
    srcs = srcs + deps,
    outs = ["%s.jar" % name],
    cmd = " ".join(cmd) +
      ("&& jar cf $@ -C {0}/files .".format(name))
  )

def kotlin_js_binary(name, srcs=[], deps=[]):
  mainName = "{}_main_jsjar".format(name)

  kotlin_js_lib(
      name = mainName,
      srcs = srcs,
      deps = deps,
      main = True
  )

  allDeps = deps + [":{}".format(mainName)]
  native.genrule(
      name = name,
      message = "Linking JS {}".format(name),
      srcs = allDeps,
      outs = [name + ".js"],
      output_to_bindir = 1,
      cmd = " && ".join([
          " ".join([
              "/home/dg/src/kotlinc/bin/kotlin-dce-js",
              "-output-dir $@.files/node_modules",
          ] + ["$(locations "+d+")" for d in allDeps]
          ),
          " (cd $@.files && " + " ".join([
              "browserify-lite",
              "--outfile out.js ",
              "{}.js".format(mainName),
          ]) + ")",
          "cp $@.files/out.js $@"
      ])
  )

def kotlin_konan_lib(name, srcs=[], deps=[]):
  cmd = ["/home/dg/src/kotlin-native-linux-0.7.1/bin/kotlinc-native",
         "-Xcoroutines=enable",
         "-produce library",
         "-g",
         "--disable devirtualization",
         "-output $@"]

  for f in srcs:
    cmd += ["$(location {})".format(f)]
  for j in deps:
    cmd += ["-repo $$(dirname $(location {}))".format(j)]
    cmd += ["-library $(location {})".format(j)]

  native.genrule(
    name = name,
    message = "Building Konan {0}".format(name),
    srcs = srcs + deps,
    outs = [name + ".klib"],
    cmd = " ".join(cmd),
  )

def kotlin_konan_binary(name, srcs=[], deps=[], main="main"):
  cmd = ["/home/dg/src/kotlin-native-linux-0.7.1/bin/kotlinc-native",
         "-produce program",
         "--purge_user_libs",
         "-entry {}".format(main),
         "-g",
         "--disable devirtualization",
         "-output $@"]

  for f in srcs:
    cmd += ["$(location {})".format(f)]
  for j in deps:
    cmd += ["-repo $$(dirname $(location {}))".format(j)]
    cmd += ["-library $(location {})".format(j)]

  native.genrule(
    name = name,
    message = "Linking Konan {0}".format(name),
    srcs = srcs + deps,
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
        "/home/dg/src/kotlin-native-linux-0.7.1/bin/cinterop",
        "-def $<",
        "-o $@",
    ])
  )
