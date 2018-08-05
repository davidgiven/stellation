package server

import interfaces.IConsole
import interfaces.withSqlTransaction
import model.GOD_OID
import utils.Flags
import utils.bind
import utils.getopt
import kotlin.system.exitProcess

class CliHandler : AbstractHandler() {
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
                catchup()
            }

            database.withSqlTransaction {
                runBlocking {
                    commandshell.call(remaining)
                }
            }
        }
    }
}


