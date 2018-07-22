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

class ErrnoException(): Exception("System call failed: errno = ${errstr}")

class KonanEnvironment : IEnvironment {
    override fun getenv(name: String): String? = platform.posix.getenv(name)?.toKString()

    override fun readStdin(bytes: Int): String {
        val array = ByteArray(bytes)
        array.usePinned { pinned ->
            var amountRead = 0
            while (amountRead < bytes) {
                val toRead = bytes - amountRead
                val i = platform.posix.read(0, pinned.addressOf(amountRead), toRead.toLong()).toInt()
                if (i == -1) {
                    throw ErrnoException()
                }
                amountRead += i
            }
        }

        return array.stringFromUtf8()
    }

    override fun writeStdout(value: String) {
        val array = value.toUtf8()
        array.usePinned { pinned ->
            var amountWritten = 0
            while (amountWritten < array.size) {
                val toWrite = array.size - amountWritten
                val i = platform.posix.write(1, pinned.addressOf(amountWritten), toWrite.toLong()).toInt()
                if (i == -1) {
                    throw ErrnoException()
                }
                amountWritten += i;
            }
        }
    }
}
