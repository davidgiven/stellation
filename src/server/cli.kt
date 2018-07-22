package server

import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.withSqlTransaction
import model.Model
import model.ObjectNotVisibleException
import model.SUniverse
import model.Timers
import model.createNewUniverse
import runtime.shared.SqlDatastore
import utils.bind
import utils.get
import utils.getopt
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

    val database = get<IDatabase>()
    val datastore = bind<IDatastore>(SqlDatastore(database))
    database.openDatabase(databaseFilename)
    datastore.initialiseDatabase()
    val model = bind(Model())
    bind(Timers())

    database.withSqlTransaction {
        model.initialiseProperties()
        bind(findOrCreateUniverse(model))
    }

    database.closeDatabase()
}

private fun findOrCreateUniverse(model: Model): SUniverse {
    try {
        return findUniverse(model)
    } catch (_: ObjectNotVisibleException) {
        return model.createNewUniverse()
    }
}


