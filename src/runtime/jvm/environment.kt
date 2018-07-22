package runtime.jvm

import interfaces.IEnvironment
import java.io.IOException

class JvmEnvironment : IEnvironment {
    override fun getenv(name: String): String? = System.getenv(name)

    override fun readStdin(bytes: Int): ByteArray {
        val array = ByteArray(bytes)

        var amountRead = 0
        while (amountRead < bytes) {
            val toRead = bytes - amountRead
            val i = System.`in`.read(array, amountRead, toRead)
            if (i == -1) {
                throw IOException("premature EOF")
            }
            amountRead += i
        }

        return array
    }

    override fun writeStdout(array: ByteArray) = System.out.write(array)
    override fun writeStdout(value: String) = System.out.print(value)
    override fun writeStderr(array: ByteArray) = System.err.write(array)
    override fun writeStderr(value: String) = System.err.print(value)
}
