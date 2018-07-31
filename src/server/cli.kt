package server

import interfaces.IConsole
import interfaces.IDatabase
import interfaces.withSqlTransaction
import model.Model
import model.ObjectNotVisibleException
import model.SUniverse
import model.createNewUniverse
import runtime.shared.CommandShell
import utils.Flags
import utils.bind
import utils.getopt
import utils.injection
import kotlin.system.exitProcess

private var databaseFilename = "stellation.sqlite"

private fun help(@Suppress("UNUSED_PARAMETER") arg: String): Nothing {
    println("Stellation6 server")
    println("Syntax: stellation [<option>] <command> [<command args>]")
    println("  --help            Shows this message")
    println("  --db=<filename>   Use this filename for the database file")
    exitProcess(0)
}

fun serveCli(argv: Array<String>) {
    val remaining = getopt(
            argv, Flags()
            .addFlag("-h", ::help)
            .addFlag("--help", ::help)
            .addString("--db", ::databaseFilename))

    withServer(databaseFilename) {
        bind<IConsole>(ServerConsole())
        val database by injection<IDatabase>()
        val model by injection<Model>()
        val commandshell by injection<CommandShell>()

        database.withSqlTransaction {
            bind(findOrCreateUniverse(model))

            runBlocking {
                commandshell.call(remaining)
            }
        }
    }
}

private fun findOrCreateUniverse(model: Model): SUniverse {
    try {
        return findUniverse(model)
    } catch (_: ObjectNotVisibleException) {
        return model.createNewUniverse()
    }
}


