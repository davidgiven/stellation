package server

import interfaces.ICommandDispatcher
import interfaces.ILogger
import runtime.shared.ServerMessage
import utils.injection
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.coroutines.experimental.startCoroutine

abstract class HttpStatusException(val status: Int, message: String) : Exception(message)
class BadRequestException : HttpStatusException(400, "Bad Request")
class UnauthorizedException : HttpStatusException(401, "Unauthorized")
class InternalServerErrorException : HttpStatusException(500, "Internal Server Error")

class RemoteServer {
    val commandDispatcher by injection<ICommandDispatcher>()
    val logger by injection<ILogger>()

    fun onMessageReceived(input: ServerMessage, output: ServerMessage) {
        try {
            if (input.hasCommandInput()) {
                val argv = input.getCommandInput()
                val command = commandDispatcher.resolve(argv)

                runBlocking { command.run() }

                output.setCommandOutput(command.output)
            }
        } catch (e: Exception) {
            logger.println(e.toString())
            output.setError(e.message)
        }
    }
}

private val completionContinuation = object : Continuation<Unit> {
    override val context = EmptyCoroutineContext

    override fun resume(value: Unit) {
        throw RuntimeException("this coroutine cannot suspend")
    }

    override fun resumeWithException(exception: Throwable) {
        throw exception
    }
}

private fun runBlocking(block: suspend () -> Unit) {
    block.startCoroutine(completionContinuation)
}
