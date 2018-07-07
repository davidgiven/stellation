def kotlin_jvm_lib(name, srcs=[], deps=[]):
  cmd = ["/home/dg/src/kotlinc/bin/kotlinc-jvm",
         "-d $@"]
  for f in srcs:
    cmd += [f]
  for j in deps:
    cmd += ["-cp $(location %s)" % j]

  native.genrule(
    name = name,
    message = "Building JVM {0}".format(name),
    srcs = srcs + deps,
    outs = [name + ".jar"],
    cmd = " ".join(cmd)
  )

def kotlin_jvm_binary(name, deps=[], libs=[], **kwargs):
  jars = " ".join(["$(location {})".format(dep) for dep in deps])

  native.java_import(
      name = name + "_jars",
      jars = deps
  )

  native.java_binary(
      name = name,
      runtime_deps = [":{}_jars".format(name)] + libs,
      **kwargs
  )

def kotlin_js_lib(name, srcs=[], deps=[], main=False):
  cmd = ["/home/dg/src/kotlinc/bin/kotlinc-js",
         "-module-kind commonjs",
         "-output {0}/files/{0}.js".format(name),
         "-meta-info"]
  if not main:
    cmd += ["-main noCall"]

  for f in srcs:
    cmd += [f]
  for j in deps:
    cmd += ["-libraries $(location %s)" % j]

  native.genrule(
    name = name,
    message = "Building JS {0}".format(name),
    srcs = srcs + deps,
    outs = ["%s.jar" % name],
    cmd = " ".join(cmd) +
      ("&& jar -c --file $@ -C {0}/files .".format(name))
  )

def kotlin_js_binary(name, deps=[]):
  native.genrule(
      name = name,
      message = "Linking JS {}".format(name),
      srcs = deps,
      outs = [name + ".js"],
      cmd = " && ".join([
          " ".join([
              "/home/dg/src/kotlinc/bin/kotlin-dce-js",
              "-output-dir $@.files/node_modules",
          ] + ["$(locations "+d+")" for d in deps]
          ),
          "echo $$(realpath $@.files)",
          "ls -l $@.files",
          " (cd $@.files && " + " ".join([
              "browserify-lite",
              "--outfile out.js ",
              "main_jsjar.js",
          ]) + ")",
          "cp $@.files/out.js $@"
      ])
  )

def kotlin_konan_lib(name, srcs=[], deps=[]):
  cmd = ["/home/dg/src/kotlin-native-linux-0.7.1/bin/kotlinc-native",
         "-produce library",
         "-output $@"]

  for f in srcs:
    cmd += [f]
  for j in deps:
    cmd += ["-library $(location %s)" % j]

  native.genrule(
    name = name,
    message = "Building Konan {0}".format(name),
    srcs = srcs + deps,
    outs = [name + ".klib"],
    cmd = " ".join(cmd),
  )

def kotlin_konan_binary(name, srcs=[], deps=[]):
  cmd = ["/home/dg/src/kotlin-native-linux-0.7.1/bin/kotlinc-native",
         "-produce program",
         "--purge_user_libs",
         "-output $@"]

  for f in srcs:
    cmd += [f]
  for j in deps:
    cmd += ["-repo $$(dirname $(location %s)) -library $(location %s)" % (j, j)]

  native.genrule(
    name = name,
    message = "Linking Konan {0}".format(name),
    srcs = srcs + deps,
    outs = [name + ".kexe"],
    cmd = " ".join(cmd),
  )

