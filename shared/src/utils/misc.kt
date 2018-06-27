package utils

class UnimplementedError: Error("not implemented")

class FatalError(message: String): Error(message)

fun UNIMPLEMENTED() {
    throw UnimplementedError()
}
