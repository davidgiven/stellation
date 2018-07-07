package server

import interfaces.context

fun main(argv: Array<String>) {
    val database = context.database!!
    database.openDatabase("stellation.sqlite")
    database.closeDatabase()
}

