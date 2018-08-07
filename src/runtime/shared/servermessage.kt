package runtime.shared

import interfaces.CommandMessage
import utils.Fault
import utils.Message
import utils.Oid
import utils.SyncMessage

private const val CMDINPUT = "cmdinput"
private const val CMDOUTPUT = "cmdoutput"
private const val FAULT = "fault"
private const val PASSWORD = "password"
private const val USERNAME = "username"
private const val PLAYEROID = "oid"
private const val STATUS = "status"
private const val SYNC = "sync"
private const val CLOCK = "clock"
private const val SESSION = "session"


class ServerMessage : Message {
    constructor(): super()
    constructor(serialised: String): super(serialised)

    fun hasFault() = contains(FAULT)
    fun setFault(fault: Fault) = set(FAULT, fault.serialise())
    fun getFault(): Fault = Fault(get(FAULT))

    fun hasCommandInput() = contains(CMDINPUT)

    fun setCommandInput(argv: List<String>) {
        val message = Message()
        for (arg in argv) {
            message.addString(arg)
        }
        set(CMDINPUT, message.serialise())
    }

    fun getCommandInput(): List<String> {
        val encoded: String = get(CMDINPUT)
        return Message(encoded).toList()
    }

    fun hasCommandOutput() = contains(CMDOUTPUT)

    fun setCommandOutput(output: CommandMessage) {
        set(CMDOUTPUT, output.serialise())
    }

    fun getCommandOutput(): CommandMessage {
        val encoded: String = get(CMDOUTPUT)
        return CommandMessage(encoded)
    }

    fun setSyncMessage(sync: SyncMessage) {
        set(SYNC, sync.serialise())
    }

    fun getSyncMessage(): SyncMessage {
        val encoded: String = get(SYNC)
        return SyncMessage(encoded)
    }

    fun hasSyncSession() = contains(SESSION)
    fun getSyncSession(): Int = getInt(SESSION)
    fun setSyncSession(session: Int) = setInt(SESSION, session)

    fun setUsername(username: String) = setString(USERNAME, username)
    fun getUsername(): String = getString(USERNAME)

    fun setPassword(password: String) = setString(PASSWORD, password)
    fun getPassword(): String = getString(PASSWORD)

    fun setPlayerOid(playerOid: Oid) = setInt(PLAYEROID, playerOid)
    fun getPlayerOid(): Oid = getInt(PLAYEROID)

    fun setClock(clock: Double) = setDouble(CLOCK, clock)
    fun getClock() = getDouble(CLOCK)
}

