package runtime.js

import interfaces.CommandMessage
import interfaces.IServerInterface
import interfaces.RemoteCommandExecutionException
import org.w3c.xhr.XMLHttpRequest
import runtime.shared.ServerMessage
import utils.Codec
import utils.Mailbox
import utils.injection

class RemoteServerInterface : IServerInterface {
    private val codec by injection<Codec>()

    override suspend fun executeCommand(argv: List<String>): CommandMessage {
        val sendMessage = ServerMessage()
        sendMessage.setCommandInput(argv)

        val mailbox: Mailbox<ServerMessage> = Mailbox()
        val xhr = XMLHttpRequest()
        xhr.onreadystatechange = {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                val receiveMessage = ServerMessage()
                try {
                    if (xhr.status.toInt() == 200) {
                        receiveMessage.setFromMap(codec.decode(xhr.responseText))
                    } else {
                        throw RemoteCommandExecutionException("network error ${xhr.status}")
                    }
                } catch (e: Exception) {
                    receiveMessage.setError("network error ${xhr.status}")
                }
                mailbox.post(receiveMessage)
                kickScheduler()
            }
        }
        xhr.open("POST", "http://localhost/~dg/cgi-bin/stellation.cgi", true)
        xhr.send(codec.encode(sendMessage.toMap()))

        val receiveMessage = mailbox.wait()
        val error = receiveMessage.getError()
        if (error != null) {
            throw RemoteCommandExecutionException(error)
        }

        return receiveMessage.getCommandOutput()
    }
}

