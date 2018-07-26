package runtime.shared

import interfaces.CommandMessage
import utils.Codec
import utils.Message
import utils.add
import utils.get
import utils.injection
import utils.set

private const val ERROR = "_error"
private const val CMDINPUT = "_cmdinput"
private const val CMDOUTPUT = "_cmdoutput"

class ServerMessage : Message() {
    val codec by injection<Codec>()

    fun setError(error: String?) = set(ERROR, error)
    fun getError(): String? = get(ERROR)

    fun hasCommandInput(): Boolean = CMDINPUT in this

    fun setCommandInput(argv: List<String>) {
        val message = Message()
        for (arg in argv) {
            message.add(arg)
        }
        val encoded = codec.encode(message.toMap())
        set(CMDINPUT, encoded)
    }

    fun getCommandInput(): List<String> {
        val encoded: String = get(CMDINPUT)
        val message = Message()
        message.setFromMap(codec.decode(encoded))
        return message.toList()
    }

    fun hasCommandOutput(): Boolean = CMDOUTPUT in this

    fun setCommandOutput(output: CommandMessage) {
        val encoded = codec.encode(output.toMap())
        set(CMDOUTPUT, encoded)
    }

    fun getCommandOutput(): CommandMessage {
        val encoded: String = get(CMDOUTPUT)
        val message = CommandMessage()
        message.setFromMap(codec.decode(encoded))
        return message
    }
}

