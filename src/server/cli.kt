package server

import interfaces.IAuthenticator
import interfaces.IConsole
import interfaces.IDatabase
import interfaces.withSqlTransaction
import model.GOD_OID
import model.Model
import model.SUniverse
import model.createNewUniverse
import runtime.shared.CommandShell
import utils.Fault
import utils.Flags
import utils.bind
import utils.getopt
import utils.injection
import kotlin.system.exitProcess

class CliHandler {
    private val authenticator by injection<IAuthenticator>()
    private val database by injection<IDatabase>()
    private val model by injection<Model>()
    private val commandshell by injection<CommandShell>()

    private var databaseFilename = "stellation.sqlite"
    private var userOid = GOD_OID

    private fun help(@Suppress("UNUSED_PARAMETER") arg: String): Nothing {
        println("Stellation6 server")
        println("Syntax: stellation [<option>] <command> [<command args>]")
        println("  --help            Shows this message")
        println("  --db=<filename>   Use this filename for the database file")
        exitProcess(0)
    }

    fun serve(argv: Array<String>) {
        val remaining = getopt(
                argv, Flags()
                .addFlag("-h", ::help)
                .addFlag("--help", ::help)
                .addInt("--user", ::userOid)
                .addString("--db", ::databaseFilename))

        withServer(databaseFilename) {
            bind<IConsole>(ServerConsole())

            authenticator.setAuthenticatedPlayer(userOid)

            database.withSqlTransaction {
                bind(findOrCreateUniverse(model))

                runBlocking {
                    commandshell.call(remaining)
                }
            }
        }
    }
}

private fun findOrCreateUniverse(model: Model): SUniverse {
    try {
        return findUniverse(model)
    } catch (f: Fault) {
        return model.createNewUniverse()
    }
}


