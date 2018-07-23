package interfaces

class AuthenticationFailedException : Exception("authentication failed")

interface IAuthenticator {
    var currentUser: Oid

    fun initialiseDatabase()
    fun withLoggedInUser(username: String, credentials: String, callback: () -> Unit)
}
