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
  cmd = ["tar cf $@"]
  cmd += ["$(location {})".format(src) for src in srcs]

  native.genrule(
    name = name,
    message = "Building JS {0}".format(name),
    srcs = srcs + deps,
    outs = ["%s.tar" % name],
    cmd = " ".join(cmd)
  )

def kotlin_js_binary(name, srcs=[], deps=[]):
  cmds = ["mkdir $@.srcs"]
  for src in srcs:
      cmds += ["tar xf $(location {0}) -C $@.srcs".format(src)]

  cmds += [" ".join(["/home/dg/src/kotlinc/bin/kotlinc-js",
         "-Xcoroutines=enable",
         "-module-kind commonjs",
         "-output $@",
         "-meta-info",
         "$$(find $@.srcs -name *.kt)"])]

  native.genrule(
    name = "{}_compiled".format(name),
    message = "Building JS {0}".format(name),
    srcs = srcs,
    outs = ["{0}.main.js".format(name)],
    cmd = " && ".join(cmds)
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
              "-dev-mode",
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
              "{}.main.js".format(name),
              "out.js"
          ]) + ")",
          "cp $@.files/out.js $@"
      ])
  )

def kotlin_konan_lib(name, srcs=[], deps=[]):
  cmd = ["/home/dg/src/kotlin-native-linux-0.8.1/bin/kotlinc-native",
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
  cmd = ["/home/dg/src/kotlin-native-linux-0.8.1/bin/kotlinc-native",
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
        "/home/dg/src/kotlin-native-linux-0.8.1/bin/cinterop",
        "-def $<",
        "-o $@",
    ])
  )
