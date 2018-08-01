package client

import interfaces.AuthenticationFailedException
import ui.LoginForm
import utils.Job
import kotlin.browser.document
import interfaces.IConsole
import interfaces.IClientInterface
import interfaces.ICommandDispatcher
import interfaces.log
import ui.AlertForm
import utils.inject
import utils.injection

private var loggedIn = false

fun startGame() {
    doLogin()
}

private fun doLogin() {
    Job {
        val cookies = inject<Cookies>()

        while (true) {
            val defaultUsername = cookies["username"] ?: ""
            val defaultPassword = cookies["password"] ?: ""

            val loginData = LoginForm(defaultUsername, defaultPassword).execute()
            if (!loginData.canceled) {
                try {
                    val client = inject<IClientInterface>()
                    client.setCredentials(loginData.username!!, loginData.password!!)

                    val commandDispatcher = inject<ICommandDispatcher>()
                    val command = commandDispatcher.resolve(listOf("ping"))
                    command.run()

                    cookies["username"] = loginData.username!!
                    cookies["password"] = loginData.password!!
                    loggedIn = true
                    doGame()
                    break
                } catch (_: AuthenticationFailedException) {
                    AlertForm("Login failed", "Username or password unrecognised.").execute()
                }
            }
        }
    }
}

private fun doGame() {
    Job {
        val console by injection<IConsole>()
        console.println("Welcome to Stellation VI.")
        console.println("Try 'help' if you're feeling lucky.")
    }
}
