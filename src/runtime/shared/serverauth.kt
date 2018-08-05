package server

import interfaces.IAuthenticator
import interfaces.IDatabase
import interfaces.ILogger
import utils.Oid
import interfaces.throwAuthenticationFailedException
import model.Model
import model.SPlayer
import model.currentPlayer
import utils.BCrypt
import utils.injection

class ServerAuthenticator : IAuthenticator {
    private val database by injection<IDatabase>()
    private val model by injection<Model>()
    private val bcrypt by injection<BCrypt>()
    private val logger by injection<ILogger>()

    override var currentPlayerOid: Oid = 0

    override fun initialiseDatabase() {
        database.executeSql(
                """
                CREATE TABLE IF NOT EXISTS players (
                    username TEXT PRIMARY KEY,
                    oid NOT NULL REFERENCES objects(oid) ON DELETE CASCADE
                )
            """)
    }

    override fun authenticatePlayer(username: String, password: String, callback: () -> Unit) {
        check(currentPlayerOid == 0)

        try {
            currentPlayerOid = database.sqlStatement("SELECT oid FROM players WHERE username = ?")
                    .bindString(1, username)
                    .executeSimpleQuery()
                    ?.get("oid")
                    ?.getOid()
                    ?: throwAuthenticationFailedException()

            if (!bcrypt.checkpw(password, model.currentPlayer().password_hash)) {
                throwAuthenticationFailedException()
            }

            callback()
        } finally {
            currentPlayerOid = 0
        }
    }

    override fun setAuthenticatedPlayer(oid: Oid) {
        check(currentPlayerOid == 0)
        currentPlayerOid = oid
    }

    override fun registerPlayer(username: String, playerOid: Oid) {
        database.sqlStatement(
                """
                INSERT OR REPLACE INTO players (username, oid) VALUES (?, ?)
                """)
                .bindString(1, username)
                .bindOid(2, playerOid)
                .executeStatement()
    }

    override fun setPassword(playerOid: Oid, password: String) {
        val player = model.loadObject(playerOid, SPlayer::class)
        val salt = bcrypt.gensalt(4)
        val hashed = bcrypt.hashpw(password, salt)
        player.password_hash = hashed
    }
}
