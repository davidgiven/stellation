package interfaces

class AuthenticationFailedException : Exception("authentication failed")
class NobodyLoggedInException: Exception("nobody is logged in")
class PermissionDeniedException: Exception("you're not allowed to do that")

interface IAuthenticator {
    var currentUser: Oid

    fun initialiseDatabase()

    fun setAuthenticatedUser(oid: Oid)

    fun authenticateUser(username: String, password: String, callback: () -> Unit)
}
