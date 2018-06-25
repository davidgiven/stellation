package server.main

interface RuntimeEnvironment {
    fun log(s: String);
}

var runtimeEnvironment: RuntimeEnvironment? = null;

fun run(env: RuntimeEnvironment) {
    runtimeEnvironment = env;
    log("Hello, multiplatform world!");
}

fun log(s: String) {
    runtimeEnvironment!!.log(s);
}

