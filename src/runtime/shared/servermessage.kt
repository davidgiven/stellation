package runtime.shared

import interfaces.CommandMessage
import interfaces.Oid
import utils.Codec
import utils.Message
import utils.add
import utils.get
import utils.injection
import utils.set

private const val CMDINPUT = "cmdinput"
private const val CMDOUTPUT = "cmdoutput"
private const val ERROR = "error"
private const val PASSWORD = "password"
private const val USERNAME = "username"
private const val PLAYEROID = "oid"

class ServerMessage : Message() {
    val codec by injection<Codec>()

    fun hasError() = contains(ERROR)
    fun setError(error: String) = set(ERROR, error)
    fun getError(): String = get(ERROR)

    fun hasCommandInput() = contains(CMDINPUT)

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

    fun hasCommandOutput() = contains(CMDOUTPUT)

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

    fun setUsername(username: String) = set(USERNAME, username)
    fun getUsername(): String = get(USERNAME)

    fun setPassword(password: String) = set(PASSWORD, password)
    fun getPassword(): String = get(PASSWORD)

    fun setPlayerOid(playerOid: Oid) = set(PLAYEROID, playerOid)
    fun getPlayerOid(): Oid = get(PLAYEROID)
}

