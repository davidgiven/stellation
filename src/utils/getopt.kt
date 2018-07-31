package utils

import kotlin.reflect.KMutableProperty0

const val FILE_OPTION = " <file>"

open class GetoptException(message: String) : RuntimeException(message)

class DuplicateFlagException(arg: String) : GetoptException(
        "flag '$arg' is already defined"
)

class MissingFlagException(arg: String) : GetoptException(
        "parameter for flag '$arg' is missing (try --help)"
)

class UnrecognisedFlagException(arg: String) : GetoptException(
        "unrecognised flag '$arg' (try --help)"
)

class InvalidFlagValueException(arg: String) : GetoptException(
        "invalid value for flag '$arg' (try --help)"
)

abstract class AbstractFlag(val name: String) {
    abstract fun set(value: String): Boolean
}

class Flag(name: String, val callback: (String) -> Unit) : AbstractFlag(name) {
    override fun set(input: String): Boolean {
        callback(input)
        return false
    }
}

abstract class VarFlag<T>(name: String, val property: KMutableProperty0<T>) : AbstractFlag(name) {
    abstract fun translate(input: String): T

    override fun set(input: String): Boolean {
        val value = translate(input)
        property.set(value)
        return true
    }
}

class BooleanFlag(name: String, property: KMutableProperty0<Boolean>) : VarFlag<Boolean>(name, property) {
    override fun translate(input: String) = when (input) {
        "true"  -> true
        "false" -> false
        else    -> throw InvalidFlagValueException(name)
    }
}

class IntFlag(name: String, property: KMutableProperty0<Int>) : VarFlag<Int>(name, property) {
    override fun translate(input: String): Int {
        try {
            return input.toInt()
        } catch (_: NumberFormatException) {
            throw InvalidFlagValueException(name)
        }
    }
}

class StringFlag(name: String, property: KMutableProperty0<String>) : VarFlag<String>(name, property) {
    override fun translate(input: String) = input
}

class Flags {
    var map: Map<String, AbstractFlag> = emptyMap()

    fun add(flag: AbstractFlag): Flags {
        if (flag.name in map) {
            throw DuplicateFlagException(flag.name)
        }
        map += flag.name to flag
        return this
    }

    fun addFlag(name: String, callback: (String) -> Unit) =
            add(Flag(name, callback))

    fun addBoolean(name: String, property: KMutableProperty0<Boolean>) =
            add(BooleanFlag(name, property))

    fun addInt(name: String, property: KMutableProperty0<Int>) =
            add(IntFlag(name, property))

    fun addString(name: String, property: KMutableProperty0<String>) =
            add(StringFlag(name, property))
}

fun getopt(argv: List<String>, flags: Flags): List<String> {
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
                value = argv.getOrElse(index + 1, { "" })
                consume = true
            } else {
                key = arg.slice(0..(equals - 1))
                value = arg.substring(equals + 1)
            }
        } else if (arg.startsWith("-")) {
            // -fbar or -f bar
            if (arg.length == 2) {
                key = arg
                value = argv.getOrElse(index + 1, { "" })
                consume = true
            } else {
                key = arg.slice(0..1)
                value = arg.substring(2)
            }
        } else {
            return argv.subList(index, argv.size)
        }

        val flag = flags.map[key] ?: throw UnrecognisedFlagException(key)
        val consumed = flag.set(value)
        if (consume) {
            if (consumed) {
                index++
            }
            if (index >= argv.size) {
                throw MissingFlagException(key)
            }
        }
        index++
    }

    return emptyList()
}

fun getopt(argv: Array<String>, flags: Flags) = getopt(argv.toList(), flags)
