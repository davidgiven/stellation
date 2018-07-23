package server

import interfaces.IAuthenticator
import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.IEnvironment
import interfaces.ILogger
import interfaces.withSqlTransaction
import model.Model
import model.ObjectNotVisibleException
import model.Timers
import runtime.shared.SqlDatastore
import utils.FormDecoder
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
    val model = bind(Model())
    bind(Timers())
    val auth = bind<IAuthenticator>(ServerAuthenticator())

    database.withSqlTransaction {
        model.initialiseProperties()
        auth.initialiseDatabase()
    }

    try {
        bind(findUniverse(model))
    } catch (_: ObjectNotVisibleException) {
    }

    try {
        get<ILogger>().println("server ready")
        callback()
    } finally {
        database.closeDatabase()
    }
}