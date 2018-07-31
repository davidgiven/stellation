package commands

class PingCommand : AbstractRemoteCommand() {
    override val name = "ping"
    override val description = "pings the server for a status update"

    override fun serverRun() {
        output.setSuccess(true)
    }
}
