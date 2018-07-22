package server

import interfaces.IDatabase
import interfaces.Oid
import utils.get
import model.Model

class AuthenticationFailedException : Exception("authentication failed")

class Authenticator(val database: IDatabase = get(), val model: Model = get()) {
    var currentUser: Oid = 0

    fun initialiseDatabase() {
        database.executeSql(
                """
                CREATE TABLE IF NOT EXISTS players (
                    username TEXT PRIMARY KEY,
                    oid NOT NULL REFERENCES objects(oid) ON DELETE CASCADE
                )
            """)
    }

    fun withLoggedInUser(username: String, credentials: String, callback: () -> Unit) {
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

