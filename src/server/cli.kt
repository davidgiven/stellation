package server

import interfaces.IDatabase
import interfaces.withSqlTransaction
import model.Model
import model.ObjectNotVisibleException
import model.SUniverse
import model.createNewUniverse
import utils.bind
import utils.getopt
import utils.inject
import kotlin.system.exitProcess

private var databaseFilename = "stellation.sqlite"

private fun help(@Suppress("UNUSED_PARAMETER") arg: String): Nothing {
    println("Stellation6 server")
    println("  --help            Shows this message")
    println("  --db=<filename>   Use this filename for the database file")
    exitProcess(0)
}

fun serveCli(argv: Array<String>) {
    getopt(argv, mapOf<String, (String) -> Int>(
            "-h" to ::help,
            "--help" to ::help,
            "--db" to { f -> databaseFilename = f; 1 }
    ))

    withServer(databaseFilename) {
        val database = inject<IDatabase>()
        val model = inject<Model>()

        database.withSqlTransaction {
            bind(findOrCreateUniverse(model))
        }
    }
}

private fun findOrCreateUniverse(model: Model): SUniverse {
    try {
        return findUniverse(model)
    } catch (_: ObjectNotVisibleException) {
        return model.createNewUniverse()
    }
}


