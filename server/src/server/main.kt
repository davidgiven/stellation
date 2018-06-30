package server

import datastore.closeDatabase
import datastore.initialiseDatabase
import datastore.openDatabase
import datastore.withSqlTransaction
import utils.getopt
import runtime.println
import runtime.exit
import shared.ObjectNotVisibleException
import shared.SUniverse
import shared.bind
import shared.create
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
        return SUniverse().bind(1)
    } catch (_: ObjectNotVisibleException) {
        log("creating new universe")
        var universe = SUniverse().create()
        check(universe.oid == 1)
        universe.initialiseUniverse()
        return universe
    }
}
