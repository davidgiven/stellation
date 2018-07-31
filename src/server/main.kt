package server

import commands.CommandDispatcher
import interfaces.IAuthenticator
import interfaces.IClientInterface
import interfaces.ICommandDispatcher
import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.IEnvironment
import interfaces.ITime
import interfaces.withSqlTransaction
import model.Model
import model.Timers
import runtime.shared.CommandShell
import runtime.shared.LocalClientInterface
import runtime.shared.SqlDatastore
import utils.Codec
import utils.Random
import utils.bind
import utils.inject
import utils.injection

fun main(argv: Array<String>) {
    bind(Codec())
    bind<IDatastore>(SqlDatastore())
    bind(Model())
    bind(Timers())
    bind<IAuthenticator>(ServerAuthenticator())
    bind<IClientInterface>(LocalClientInterface())
    bind(CommandShell())
    bind<ICommandDispatcher>(CommandDispatcher())
    bind(RemoteServer())

    val time = inject<ITime>()
    bind(Random(time.nanotime()))

    val environment by injection<IEnvironment>()
    if (environment.getenv("GATEWAY_INTERFACE") != null) {
        serveCgi()
    } else {
        serveCli(argv)
    }
}

fun withServer(dbfile: String, callback: () -> Unit) {
    val database by injection<IDatabase>()
    val datastore by injection<IDatastore>()
    database.openDatabase(dbfile)
    datastore.initialiseDatabase()
    val model by injection<Model>()
    val auth by injection<IAuthenticator>()

    database.withSqlTransaction {
        model.initialiseProperties()
        auth.initialiseDatabase()
    }

    try {
        callback()
    } finally {
        database.closeDatabase()
    }
}