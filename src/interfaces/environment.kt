package interfaces

interface IEnvironment {
    fun getenv(name: String): String?
    fun readStdin(bytes: Int): String
    fun writeStdout(value: String)
}
