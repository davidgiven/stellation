package server

import interfaces.ICommandDispatcher
import interfaces.log
import runtime.shared.ServerMessage
import utils.Fault
import utils.FaultDomain.NETWORK
import utils.injection

fun throwUnauthorizedException(): Nothing = throw Fault(NETWORK).withStatus(401).withDetail("Unauthorized")

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
        } catch (f: Fault) {
            log(f.toString())
            output.setFault(f)
        }
    }
}
