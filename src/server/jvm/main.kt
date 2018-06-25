package server.jvm;

fun main(argv: Array<String>) {
    var env = object: server.main.RuntimeEnvironment {
        override fun log(s: String) {
            System.err.println(s)
        }
    };
    server.main.run(env)
}

