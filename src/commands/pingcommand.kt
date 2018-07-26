package commands

import utils.GetoptCallback

class PingCommand : AbstractRemoteCommand() {
    override val name = "ping"
    override val description = "pings the server for a status update"

    override val options: Map<String, GetoptCallback> = emptyMap()

    override fun serverRun() {
        output.setSuccess(true)
    }
}
