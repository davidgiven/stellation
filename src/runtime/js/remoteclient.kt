package runtime.js

import interfaces.IAuthenticator
import interfaces.IClientInterface
import interfaces.IClock
import interfaces.ICommand
import interfaces.ISyncer
import interfaces.throwAuthenticationFailedException
import org.w3c.xhr.XMLHttpRequest
import runtime.shared.ServerMessage
import utils.Codec
import utils.Fault
import utils.FaultDomain.NETWORK
import utils.Mailbox
import utils.injection

class RemoteClientInterface : IClientInterface {
    private val codec by injection<Codec>()
    private val authenticator by injection<IAuthenticator>()
    private val clock by injection<IClock>()
    private val syncer by injection<ISyncer>()

    private var username = ""
    private var password = ""

    override fun setCredentials(username: String, password: String) {
        this.username = username
        this.password = password
    }

    override suspend fun executeCommand(command: ICommand) {
        val sendMessage = ServerMessage()
        sendMessage.setCommandInput(command.argv)
        sendMessage.setUsername(username)
        sendMessage.setPassword(password)
        sendMessage.setClock(clock.getTime())

        val mailbox: Mailbox<ServerMessage> = Mailbox()
        val xhr = XMLHttpRequest()
        xhr.onreadystatechange = {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                val receiveMessage = ServerMessage()
                val status = xhr.status.toInt()
                try {
                    when (status) {
                        200 ->
                            receiveMessage.setFromMap(codec.decode(xhr.responseText))
                        401 ->
                            throwAuthenticationFailedException()
                        else ->
                            throw Fault(NETWORK).withStatus(status).withDetail("network error")
                    }
                } catch (f: Fault) {
                    receiveMessage.setFault(f)
                }
                mailbox.post(receiveMessage)
                kickScheduler()
            }
        }
        xhr.open("POST", "http://localhost/~dg/cgi-bin/stellation.cgi", true)
        xhr.send(codec.encode(sendMessage.toMap()))

        val receiveMessage = mailbox.wait()
        if (receiveMessage.hasFault()) {
            throw receiveMessage.getFault()
        }

        authenticator.setAuthenticatedPlayer(receiveMessage.getPlayerOid())
        syncer.importSyncPacket(receiveMessage.getSyncMessage())
        clock.setTime(receiveMessage.getClock())
        command.output = receiveMessage.getCommandOutput()
    }
}
