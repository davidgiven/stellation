package server

import interfaces.IAuthenticator
import interfaces.IEnvironment
import interfaces.IUtf8
import model.Model
import model.SUniverse
import utils.Codec
import utils.get

open class BadCgiException(s: String) : Exception("Bad CGI request: $s")

class CgiRequest(val environment: IEnvironment = get(), val codec: Codec = get(), val utf8: IUtf8 = get()) {
    var parameters: Map<String, String> = emptyMap()

    init {
        if (environment.getenv("REQUEST_METHOD") != "POST") {
            throw BadCgiException("request is not POST")
        }

        val contentLength = environment.getenv("CONTENT_LENGTH")?.toInt()
                ?: throw BadCgiException("missing content length")

        val body = environment.readStdin(contentLength)
        parameters = codec.decode(utf8.toString(body))
    }
}

class CgiResponse(val environment: IEnvironment = get(), val codec: Codec = get()) {
    var headers: Map<String, String> = emptyMap()
    var body: Map<String, String> = emptyMap()

    fun write() {
        headers.forEach { e ->
            environment.writeStdout(e.key)
            environment.writeStdout(": ")
            environment.writeStdout(e.value)
            environment.writeStdout("\n")
        }
        environment.writeStdout("\n")
        environment.writeStdout(codec.encode(body))
        body.forEach { environment.writeStdout("$it\n") }
    }
}

fun serveCgi() {
    try {
        var request = CgiRequest()
        var response = CgiResponse()

        withServer("/home/dg/nonshared/stellation/stellation.sqlite") {
            val authenticator: IAuthenticator = get()
            authenticator.withLoggedInUser("foo", "bar") {
            }
        }

        response.headers += "Content-type" to "text/plain; charset=utf-8"
        request.parameters.forEach { e ->
            response.body += e.key to e.value
        }
        response.write()
    } catch (e: Exception) {
        kotlin.io.println("Content-type: text/plain; charset=utf-8")
        kotlin.io.println("")
        throw e
    }
}

fun findUniverse(model: Model) = model.loadObject(1, SUniverse::class)

