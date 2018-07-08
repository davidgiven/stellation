package server

import interfaces.IDatabase
import model.Model
import utils.bind
import utils.get

fun main(argv: Array<String>) {
    val database = get<IDatabase>()
    database.openDatabase("stellation.sqlite")
    bind(Model())
    database.closeDatabase()
}

