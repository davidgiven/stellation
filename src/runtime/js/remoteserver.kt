package runtime.js

import interfaces.IServerInterface
import interfaces.RemoteCommandExecutionException
import org.w3c.xhr.XMLHttpRequest
import utils.Codec
import utils.Mailbox
import utils.Parameters
import utils.add
import utils.getError
import utils.injection
import utils.set
import utils.setError

class RemoteServerInterface : IServerInterface {
    private val codec by injection<Codec>()

    override suspend fun executeCommand(argv: List<String>): Parameters {
        val sendParameters = Parameters()
        for (arg in argv) {
            sendParameters.add(arg)
        }
        sendParameters["_rpc"] = "execute"

        val encoded = codec.encode(sendParameters.toMap())

        val mailbox: Mailbox<Parameters> = Mailbox()
        val xhr = XMLHttpRequest()
        xhr.onreadystatechange = {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                try {
                    if (xhr.status.toInt() == 200) {
                        val receiveParameters = Parameters(codec.decode(xhr.responseText))
                        mailbox.post(receiveParameters)
                    } else {
                        throw RemoteCommandExecutionException("network error ${xhr.status}")
                    }
                } catch (e: Exception) {
                    val receiveParameters = Parameters()
                    receiveParameters.setError("network error ${xhr.status}")
                    mailbox.post(receiveParameters)
                    kickScheduler()
                }
            }
        }
        xhr.open("POST", "http://localhost/~dg/cgi-bin/stellation.cgi", true)
        xhr.send(encoded)

        val receiveParameters = mailbox.wait()
        val error = receiveParameters.getError()
        if (error != null) {
            throw RemoteCommandExecutionException(error)
        }
        return receiveParameters
    }
}

