package server

import interfaces.IEnvironment
import model.Model
import model.SUniverse
import utils.get

class BadCgiException(s: String) : Exception("Bad CGI request: $s")

class CgiRequest(val environment: IEnvironment = get()) {
    val contentLength: Int
    val body: String

    init {
        if (environment.getenv("REQUEST_METHOD") != "POST") {
            throw BadCgiException("request is not POST")
        }

        contentLength = environment.getenv("CONTENT_LENGTH")?.toInt()
            ?: throw BadCgiException("missing content length")

        body = environment.readStdin(contentLength)
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
        response.headers += "Content-type" to "text/plain; charset=utf-8"
        response.println(request.body)
        response.write()
    } catch (e: Exception) {
        kotlin.io.println("Content-type: text/plain; charset=utf-8")
        kotlin.io.println("")
        throw e
    }
}

fun findUniverse(model: Model) = model.loadObject(1, SUniverse::class)

