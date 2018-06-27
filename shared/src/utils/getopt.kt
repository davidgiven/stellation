package utils

const val FILE_OPTION = " <file>"

private fun unrecognisedArgument(arg: String): (String) -> Int {
    throw FatalError("unrecognised argument '${arg}' (try --help)")
}

fun getopt(argv: Array<String>, callbacks: Map<String, (String) -> Int>) {
    var index = 0
    while (index < argv.size) {
        val arg = argv[index]

        var key: String
        var value: String
        var consume = false
        if (arg.startsWith("--")) {
            val equals = arg.indexOf('=')
            if (equals == -1) {
                key = arg
                value = argv.getOrElse(index, { "" })
                consume = true
            } else {
                key = arg.slice(0..(equals - 1))
                value = arg.substring(equals + 1)
            }
        } else if (arg.startsWith("-")) {
            // -fbar or -f bar
            if (arg.length == 2) {
                key = arg
                value = argv.getOrElse(index, { "" })
                consume = true
            } else {
                key = arg.slice(0..1)
                value = arg.substring(2)
            }
        } else {
            key = FILE_OPTION
            value = arg
        }

        val callback = callbacks.getOrElse(key, { unrecognisedArgument(arg) })
        val numberConsumed = callback(value)
        if (consume) {
            index += numberConsumed
        }
        index++
    }
}
