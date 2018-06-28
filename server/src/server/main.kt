package server

import datastore.initialiseDatabase
import datastore.openDatabase
import utils.getopt
import runtime.println
import runtime.exit
import shared.SUniverse
import shared.bind

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
}
