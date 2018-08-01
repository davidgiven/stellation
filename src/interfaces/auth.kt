package interfaces

class AuthenticationFailedException : Exception("authentication failed")
class NobodyLoggedInException: Exception("nobody is logged in")
class PermissionDeniedException: Exception("you're not allowed to do that")

interface IAuthenticator {
    var currentPlayerOid: Oid

    fun initialiseDatabase()

    fun setAuthenticatedPlayer(oid: Oid)
    fun authenticatePlayer(username: String, password: String, callback: () -> Unit)

    fun setPassword(playerOid: Oid, password: String)
    fun registerPlayer(username: String, playerOid: Oid)
}
