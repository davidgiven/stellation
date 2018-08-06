package commands

import model.SStar
import model.canSee
import model.currentPlayer
import utils.Flags

class StarsCommand : AbstractCommand() {
    override val name = "stars"
    override val description = "lists interesting (or otherwise) stars"

    var allOption = false

    override val flags = Flags()
            .addFlag("--all") { allOption = true }
            .addFlag("-a") { allOption = true }

    override suspend fun renderResult() {
        val player = model.currentPlayer()
        val galaxy = model.getGalaxy()
        for (o in galaxy) {
            if (o is SStar) {
                if (allOption || player.canSee(o)) {
                    console.println("  ${o.name} at ${o.x}, ${o.y} (#${o.oid})")
                }
            }
        }
    }
}
