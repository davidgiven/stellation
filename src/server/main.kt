package server

import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.IEnvironment
import interfaces.ILogger
import model.Model
import model.Timers
import runtime.shared.SqlDatastore
import utils.bind
import utils.get

fun main(argv: Array<String>) {
    val environment = get<IEnvironment>()
    if (environment.getenv("GATEWAY_INTERFACE") != null) {
        serveCgi()
    } else {
        serveCli(argv)
    }
}

fun withServer(dbfile: String, callback: ()->Unit) {
    val database = get<IDatabase>()
    val datastore = bind<IDatastore>(SqlDatastore(database))
    database.openDatabase(dbfile)
    datastore.initialiseDatabase()
    bind(Model())
    bind(Timers())

    try {
        get<ILogger>().println("server ready")
        callback()
    } finally {
        database.closeDatabase()
    }
}