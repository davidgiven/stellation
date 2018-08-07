package server

import interfaces.withSqlTransaction
import model.currentPlayer
import runtime.shared.ServerMessage
import utils.Fault
import utils.FaultDomain.NETWORK
import utils.bind

fun throwBadCgiException(s: String): Nothing = throw Fault(NETWORK).withDetail("Bad CGI request: $s")

class CgiHandler : AbstractHandler() {
    inner class CgiRequest {
        val path: String
        val input: ServerMessage

        init {
            if (environment.getenv("REQUEST_METHOD") != "POST") {
                throwBadCgiException("request is not POST")
            }

            path = environment.getenv("PATH_INFO") ?: ""

            val contentLength = environment.getenv("CONTENT_LENGTH")?.toInt()
                    ?: throwBadCgiException("missing content length")

            val bodyBytes = environment.readStdin(contentLength)
            val body = utf8.toString(bodyBytes)
            input = ServerMessage(body)
        }
    }

    inner class CgiResponse {
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
            environment.writeStdout(output.serialise())
        }
    }

    fun serve() {
        try {
            var request = CgiRequest()
            var response = CgiResponse()
            response.headers += "Content-type" to "text/plain; charset=utf-8"

            withServer("/home/dg/nonshared/stellation/stellation.sqlite") {
                bind(findUniverse(model))

                database.withSqlTransaction {
                    catchup()
                }

                val username = request.input.getUsername()
                val password = request.input.getPassword()
                authenticator.authenticatePlayer(username, password) {
                    var syncsession =
                            if (request.input.hasSyncSession())
                                request.input.getSyncSession() else
                                datastore.createSyncSession()

                    try {
                        database.withSqlTransaction {
                            response.output.setPlayerOid(authenticator.currentPlayerOid)
                            remoteServer.onMessageReceived(request.input, response.output)
                        }
                    } finally {
                        database.withSqlTransaction {
                            response.output.setClock(clock.getTime())
                            response.output.setSyncSession(syncsession)
                            response.output.setSyncMessage(
                                    syncer.exportSyncPacket(model.currentPlayer().oid, syncsession))
                        }
                    }
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
}

