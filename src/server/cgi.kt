package server

import interfaces.IAuthenticator
import interfaces.IEnvironment
import interfaces.IUtf8
import model.Model
import model.SUniverse
import runtime.shared.ServerMessage
import utils.Codec
import utils.Fault
import utils.FaultDomain.NETWORK
import utils.bind
import utils.injection

fun throwBadCgiException(s: String): Nothing = throw Fault(NETWORK).withDetail("Bad CGI request: $s")

class CgiRequest {
    private val environment by injection<IEnvironment>()
    private val codec by injection<Codec>()
    private val utf8 by injection<IUtf8>()

    val path: String
    val input = ServerMessage()

    init {
        if (environment.getenv("REQUEST_METHOD") != "POST") {
            throwBadCgiException("request is not POST")
        }

        path = environment.getenv("PATH_INFO") ?: ""

        val contentLength = environment.getenv("CONTENT_LENGTH")?.toInt()
                ?: throwBadCgiException("missing content length")

        val bodyBytes = environment.readStdin(contentLength)
        val body = utf8.toString(bodyBytes)
        input.setFromMap(codec.decode(body))
    }
}

class CgiResponse {
    private val environment by injection<IEnvironment>()
    private val codec by injection<Codec>()

    var headers: Map<String, String> = emptyMap()
    var output: ServerMessage = ServerMessage()

    fun write() {
        headers.forEach { e ->
            environment.writeStdout(e.key)
            environment.writeStdout(": ")
            environment.writeStdout(e.value)
            environment.writeStdout("\n")
        }
        environment.writeStdout("\n")
        environment.writeStdout(codec.encode(output.toMap()))
    }
}

fun serveCgi() {
    try {
        var request = CgiRequest()
        var response = CgiResponse()
        response.headers += "Content-type" to "text/plain; charset=utf-8"

        withServer("/home/dg/nonshared/stellation/stellation.sqlite") {
            val model by injection<Model>()
            val authenticator by injection<IAuthenticator>()
            val remoteServer by injection<RemoteServer>()

            bind(findUniverse(model))

            val username = request.input.getUsername()
            val password = request.input.getPassword()
            authenticator.authenticatePlayer(username, password) {
                response.output.setPlayerOid(authenticator.currentPlayerOid)
                remoteServer.onMessageReceived(request.input, response.output)
            }
        }

        response.write()
    } catch (f: Fault) {
        kotlin.io.println("Content-type: text/plain; charset=utf-8")
        kotlin.io.println("Status: ${f.status}")
        kotlin.io.println()
        kotlin.io.println(f.message)
    } catch (e: Exception) {
        kotlin.io.println("Content-type: text/plain; charset=utf-8")
        kotlin.io.println("Status: 500")
        kotlin.io.println("")
        throw e
    }
}

fun findUniverse(model: Model) = model.loadObject(1, SUniverse::class)

