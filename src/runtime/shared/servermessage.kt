package runtime.shared

import interfaces.CommandMessage
import interfaces.Oid
import model.SyncMessage
import utils.Codec
import utils.Fault
import utils.Message
import utils.add
import utils.get
import utils.injection
import utils.set

private const val CMDINPUT = "cmdinput"
private const val CMDOUTPUT = "cmdoutput"
private const val FAULT = "fault"
private const val PASSWORD = "password"
private const val USERNAME = "username"
private const val PLAYEROID = "oid"
private const val STATUS = "status"
private const val SYNC = "sync"
private const val CLOCK = "clock"

class ServerMessage : Message() {
    val codec by injection<Codec>()

    fun hasFault() = contains(FAULT)
    fun setFault(fault: Fault) = set(FAULT, fault.serialise())
    fun getFault(): Fault = Fault(get<String, String>(FAULT))

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

    fun setSyncMessage(sync: SyncMessage) {
        val encoded = codec.encode(sync.toMap())
        set(SYNC, encoded)
    }

    fun getSyncMessage(): SyncMessage {
        val encoded: String = get(SYNC)
        val message = SyncMessage()
        message.setFromMap(codec.decode(encoded))
        return message
    }

    fun setUsername(username: String) = set(USERNAME, username)
    fun getUsername(): String = get(USERNAME)

    fun setPassword(password: String) = set(PASSWORD, password)
    fun getPassword(): String = get(PASSWORD)

    fun setPlayerOid(playerOid: Oid) = set(PLAYEROID, playerOid)
    fun getPlayerOid(): Oid = get(PLAYEROID)

    fun setClock(clock: Double) = set(CLOCK, clock)
    fun getClock(): Double = get(CLOCK)
}

