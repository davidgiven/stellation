package server

import interfaces.IAuthenticator
import interfaces.Oid
import interfaces.UNIMPLEMENTED

class LocalAuthenticator : IAuthenticator {
    override var currentPlayerOid: Oid = 0

    override fun initialiseDatabase() {}

    override fun setAuthenticatedPlayer(oid: Oid) {
        currentPlayerOid = oid
    }

    override fun authenticatePlayer(username: String, password: String, callback: () -> Unit) = UNIMPLEMENTED()
    override fun setPassword(playerOid: Oid, password: String) = UNIMPLEMENTED()
    override fun registerPlayer(username: String, playerOid: Oid) = UNIMPLEMENTED()
}
