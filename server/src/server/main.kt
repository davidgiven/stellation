package server

import datastore.closeDatabase
import datastore.doesObjectExist
import datastore.initialiseDatabase
import datastore.openDatabase
import datastore.withSqlTransaction
import model.SUniverse
import model.createNewUniverse
import model.loadObject
import runtime.exit
import runtime.println
import utils.getopt

private var databaseFilename = "stellation.sqlite"

private fun help() {
    println("Stellation6 server")
    println("  --help            Shows this message")
    println("  --db=<filename>   Use this filename for the database file")
    exit(0)
}

fun main(argv: Array<String>) {

    getopt(argv, mapOf(
            "--help" to { _ -> help(); 0 },
            "--db" to { f -> databaseFilename = f; 1 }
    ))

    openDatabase(databaseFilename)
    initialiseDatabase()

    withSqlTransaction {
        var universe: SUniverse = findOrCreateUniverse()
    }

    closeDatabase()
}

private fun findOrCreateUniverse(): SUniverse =
        if (doesObjectExist(1)) loadObject(1, SUniverse::class) else createNewUniverse()
