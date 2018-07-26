package server

import interfaces.AuthenticationFailedException
import interfaces.IAuthenticator
import interfaces.IDatabase
import interfaces.Oid
import model.Model
import utils.injection

class ServerAuthenticator : IAuthenticator {
    private val database by injection<IDatabase>()
    private val model by injection<Model>()

    override var currentUser: Oid = 0

    override fun initialiseDatabase() {
        database.executeSql(
                """
                CREATE TABLE IF NOT EXISTS players (
                    username TEXT PRIMARY KEY,
                    oid NOT NULL REFERENCES objects(oid) ON DELETE CASCADE
                )
            """)
    }

    override fun withLoggedInUser(username: String, credentials: String, callback: () -> Unit) {
        check(currentUser == 0)

        try {
            currentUser = database.sqlStatement("SELECT oid FROM players WHERE username = ?")
                    .bindString(1, username)
                    .executeSimpleQuery()
                    ?.get("oid")
                    ?.getOid()
                    ?: throw AuthenticationFailedException()

            callback()
        } finally {
            currentUser = 0
        }
    }
}

