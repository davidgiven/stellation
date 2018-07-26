package server

import interfaces.IAuthenticator
import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.IEnvironment
import interfaces.ILogger
import interfaces.IServerInterface
import interfaces.withSqlTransaction
import model.Model
import model.ObjectNotVisibleException
import model.Timers
import runtime.shared.CommandShell
import runtime.shared.LocalServerInterface
import runtime.shared.SqlDatastore
import utils.bind
import utils.Codec
import utils.inject
import utils.injection

fun main(argv: Array<String>) {
    bind(Codec())

    val environment by injection<IEnvironment>()
    if (environment.getenv("GATEWAY_INTERFACE") != null) {
        serveCgi()
    } else {
        serveCli(argv)
    }
}

fun withServer(dbfile: String, callback: ()->Unit) {
    val database by injection<IDatabase>()
    val datastore = bind<IDatastore>(SqlDatastore(database))
    database.openDatabase(dbfile)
    datastore.initialiseDatabase()
    val model = bind(Model())
    bind(Timers())
    val auth = bind<IAuthenticator>(ServerAuthenticator())
    bind<IServerInterface>(LocalServerInterface())
    bind(CommandShell())

    database.withSqlTransaction {
        model.initialiseProperties()
        auth.initialiseDatabase()
    }

    try {
        bind(findUniverse(model))
    } catch (_: ObjectNotVisibleException) {
    }

    try {
        inject<ILogger>().println("server ready")
        callback()
    } finally {
        database.closeDatabase()
    }
}