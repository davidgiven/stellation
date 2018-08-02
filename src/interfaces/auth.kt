package interfaces

import utils.Fault
import utils.FaultDomain.PERMISSION

fun throwAuthenticationFailedException(): Nothing =
        throw Fault(PERMISSION).withStatus(401).withDetail("authentication failed")

fun throwNobodyLoggedInException(): Nothing = throw Fault(PERMISSION).withDetail("nobody is logged in")
fun throwPermissionDeniedException(): Nothing = throw Fault(PERMISSION).withDetail("you're not allowed to do that")

interface IAuthenticator {
    var currentPlayerOid: Oid

    fun initialiseDatabase()

    fun setAuthenticatedPlayer(oid: Oid)
    fun authenticatePlayer(username: String, password: String, callback: () -> Unit)

    fun setPassword(playerOid: Oid, password: String)
    fun registerPlayer(username: String, playerOid: Oid)
}
