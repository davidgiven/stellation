package server

import interfaces.IEnvironment
import interfaces.ILogger
import model.Model
import model.SUniverse
import utils.bind
import utils.get

open class BadCgiException(s: String) : Exception("Bad CGI request: $s")

class CgiRequest(val environment: IEnvironment = get()) {
    var parameters: Map<String, String> = emptyMap()

    init {
        if (environment.getenv("REQUEST_METHOD") != "POST") {
            throw BadCgiException("request is not POST")
        }

        val contentLength = environment.getenv("CONTENT_LENGTH")?.toInt()
                ?: throw BadCgiException("missing content length")

        val body = environment.readStdin(contentLength)
        parameters = decodeFormEncoding(body)
    }
}

class CgiResponse(val environment: IEnvironment = get()) {
    var headers: Map<String, String> = emptyMap()
    var body: List<String> = emptyList()

    fun write() {
        headers.forEach { e ->
            environment.writeStdout(e.key)
            environment.writeStdout(": ")
            environment.writeStdout(e.value)
            environment.writeStdout("\n")
        }
        environment.writeStdout("\n")
        body.forEach { environment.writeStdout("$it\n") }
    }

    fun println(s: String) {
        body += s
    }
}

fun serveCgi() {
    try {
        var request = CgiRequest()
        var response = CgiResponse()

        withServer("/home/dg/nonshared/stellation/stellation.sqlite") {
            get<Authenticator>().withLoggedInUser("foo", "bar") {
            }
        }

        response.headers += "Content-type" to "text/plain; charset=utf-8"
        request.parameters.forEach { e ->
            response.println("${e.key} = ${e.value}")
        }
        response.write()
    } catch (e: Exception) {
        kotlin.io.println("Content-type: text/plain; charset=utf-8")
        kotlin.io.println("")
        throw e
    }
}

fun findUniverse(model: Model) = model.loadObject(1, SUniverse::class)

