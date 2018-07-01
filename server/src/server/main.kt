package server

import datastore.closeDatabase
import datastore.initialiseDatabase
import datastore.openDatabase
import datastore.withSqlTransaction
import runtime.exit
import runtime.println
import shared.ObjectNotVisibleException
import shared.SUniverse
import shared.createObject
import shared.loadObject
import utils.getopt
import utils.log

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

private fun findOrCreateUniverse(): SUniverse {
    try {
        return loadObject(1, SUniverse::class)
    } catch (_: ObjectNotVisibleException) {
        log("creating new universe")
        var universe = createObject(SUniverse::class)
        check(universe.oid == 1)
        universe.initialiseUniverse()
        return universe
    }
}
