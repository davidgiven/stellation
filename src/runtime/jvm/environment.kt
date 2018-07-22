package runtime.jvm

import interfaces.IEnvironment
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets

class JvmEnvironment : IEnvironment {
    override fun getenv(name: String): String? = System.getenv(name)

    override fun readStdin(bytes: Int): String {
        var buffer = ByteBuffer.allocate(bytes)
        var channel = Channels.newChannel(System.`in`)
        while (channel.read(buffer) >= 0)
            ;

        buffer.flip()
        return String(buffer.array(), 0, buffer.limit(), StandardCharsets.UTF_8)
    }

    override fun writeStdout(value: String) {
        var ba = value.toByteArray(StandardCharsets.UTF_8)
        var buffer = ByteBuffer.wrap(ba)
        var channel = Channels.newChannel(System.out)
        while (channel.write(buffer) >= 0)
            ;
    }
}
