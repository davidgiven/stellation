package server

import datastore.IDatabase
import utils.get

fun main(argv: Array<String>) {
    val database = get<IDatabase>()
    database.openDatabase("stellation.sqlite")
    database.closeDatabase()
}

