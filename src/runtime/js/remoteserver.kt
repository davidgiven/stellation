package runtime.js

import interfaces.IServerInterface
import interfaces.RemoteCommandExecutionException
import org.w3c.xhr.XMLHttpRequest
import utils.Codec
import utils.Mailbox
import utils.Message
import utils.add
import utils.getError
import utils.injection
import utils.set
import utils.setError

class RemoteServerInterface : IServerInterface {
    private val codec by injection<Codec>()

    override suspend fun executeCommand(argv: List<String>): Message {
        val sendMessage = Message()
        for (arg in argv) {
            sendMessage.add(arg)
        }
        sendMessage["_rpc"] = "execute"

        val encoded = codec.encode(sendMessage.toMap())

        val mailbox: Mailbox<Message> = Mailbox()
        val xhr = XMLHttpRequest()
        xhr.onreadystatechange = {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                try {
                    if (xhr.status.toInt() == 200) {
                        val receiveMessage = Message(codec.decode(xhr.responseText))
                        mailbox.post(receiveMessage)
                    } else {
                        throw RemoteCommandExecutionException("network error ${xhr.status}")
                    }
                } catch (e: Exception) {
                    val receiveMessage = Message()
                    receiveMessage.setError("network error ${xhr.status}")
                    mailbox.post(receiveMessage)
                    kickScheduler()
                }
            }
        }
        xhr.open("POST", "http://localhost/~dg/cgi-bin/stellation.cgi", true)
        xhr.send(encoded)

        val receiveMessage = mailbox.wait()
        val error = receiveMessage.getError()
        if (error != null) {
            throw RemoteCommandExecutionException(error)
        }
        return receiveMessage
    }
}

