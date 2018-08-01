package server

import interfaces.ICommandDispatcher
import interfaces.ILogger
import interfaces.log
import runtime.shared.ServerMessage
import utils.injection

abstract class HttpStatusException(val status: Int, message: String) : Exception(message)
class BadRequestException : HttpStatusException(400, "Bad Request")
class UnauthorizedException : HttpStatusException(401, "Unauthorized")
class InternalServerErrorException : HttpStatusException(500, "Internal Server Error")

class RemoteServer {
    val commandDispatcher by injection<ICommandDispatcher>()

    fun onMessageReceived(input: ServerMessage, output: ServerMessage) {
        try {
            log("input = ${input.toMap()}")
            log("command input = ${input.getCommandInput()}")
            log("has command input = ${input.hasCommandInput()}")
            if (input.hasCommandInput()) {
                val argv = input.getCommandInput()
                val command = commandDispatcher.resolve(argv)

                log("calling server part of ${argv[0]}")
                command.serverRun()

                output.setCommandOutput(command.output)
            }
        } catch (e: Exception) {
            log(e.toString())
            output.setError(e.message!!)
        }
    }
}
