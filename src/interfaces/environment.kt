package interfaces

interface IEnvironment {
    fun getenv(name: String): String?
    fun readStdin(bytes: Int): ByteArray
    fun writeStderr(array: ByteArray)
    fun writeStderr(value: String)
    fun writeStdout(array: ByteArray)
    fun writeStdout(value: String)
}

