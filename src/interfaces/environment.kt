package interfaces

interface IEnvironment {
    fun getenv(name: String): String?
    fun readStdin(bytes: Int): ByteArray
    fun writeStdout(value: ByteArray)
    fun writeStdout(value: String)
}

