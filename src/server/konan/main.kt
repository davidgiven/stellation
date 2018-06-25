fun main(argv: Array<String>) {
    var env = object: server.main.RuntimeEnvironment {
        override fun log(s: String) {
            println(s);
        }
    };
    server.main.run(env);
}

