package server

import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.withSqlTransaction
import model.Model
import model.ObjectNotVisibleException
import model.SUniverse
import model.createNewUniverse
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

fun main(argv: Array<String>) {
    getopt(argv, mapOf<String, (String) -> Int>(
            "-h" to ::help,
            "--help" to ::help,
            "--db" to { f -> databaseFilename = f; 1 }
    ))

    val database = get<IDatabase>()
    val datastore = get<IDatastore>()
    database.openDatabase(databaseFilename)
    val model = bind(Model())
    database.withSqlTransaction {
        datastore.initialiseDatabase()
        model.initialiseProperties()
        bind(findOrCreateUniverse(model))
    }


    database.closeDatabase()
}

private fun findOrCreateUniverse(model: Model): SUniverse {
    try {
        return model.loadObject(1, SUniverse::class)
    } catch (_: ObjectNotVisibleException) {
        return model.createNewUniverse()
    }
}

