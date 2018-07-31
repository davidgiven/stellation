package commands

import interfaces.NobodyLoggedInException
import model.currentPlayer

class WhoAmICommand : AbstractCommand() {
    override val name = "whoami"
    override val description = "tells you who you're logged in as"

    override suspend fun run() {
        try {
            val player = model.currentPlayer()
            console.println("You are ${player.name} (#${player.oid}).")
        } catch (_: NobodyLoggedInException) {
            console.println("You're not logged in as anyone.")
        }
    }
}
