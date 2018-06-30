package utils

class UnimplementedError: Error("not implemented")

open class FatalError(message: String): Error(message)

fun UNIMPLEMENTED(): Nothing {
    throw UnimplementedError()
}

