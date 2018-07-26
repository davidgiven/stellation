package runtime.konan

import interfaces.IEnvironment
import kotlin.text.toUtf8
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned

val errno: Int
    get() = platform.posix.posix_errno()
val errstr: String
    get() = platform.posix.strerror(errno)!!.toKString()

class ErrnoException() : Exception("System call failed: errno = ${errstr}")

class KonanEnvironment : IEnvironment {
    override fun getenv(name: String): String? = platform.posix.getenv(name)?.toKString()

    fun read(fd: Int, bytes: Int): ByteArray {
        val array = ByteArray(bytes)
        array.usePinned { pinned ->
            var amountRead = 0
            while (amountRead < bytes) {
                val toRead = bytes - amountRead
                val i = platform.posix.read(fd, pinned.addressOf(amountRead), toRead.toLong()).toInt()
                if (i == -1) {
                    throw ErrnoException()
                }
                amountRead += i
            }
        }

        return array
    }

    fun write(fd: Int, array: ByteArray) {
        array.usePinned { pinned ->
            var amountWritten = 0
            while (amountWritten < array.size) {
                val toWrite = array.size - amountWritten
                val i = platform.posix.write(fd, pinned.addressOf(amountWritten), toWrite.toLong()).toInt()
                if (i == -1) {
                    throw ErrnoException()
                }
                amountWritten += i;
            }
        }
    }

    override fun readStdin(bytes: Int): ByteArray = read(0, bytes)
    override fun writeStdout(array: ByteArray) = write(1, array)
    override fun writeStderr(array: ByteArray) = write(2, array)

    override fun writeStdout(value: String) = writeStdout(value.toUtf8())
    override fun writeStderr(value: String) = writeStderr(value.toUtf8())
}
