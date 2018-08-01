package client

import ui.LoginForm
import utils.Job
import kotlin.browser.document
import interfaces.IConsole
import interfaces.IClientInterface
import utils.inject
import utils.injection

private var loggedIn = false

fun startGame() {
    doLogin()
}

private fun doLogin() {
    Job {
        while (true) {
            val loginData = LoginForm().execute()
            if (!loginData.canceled) {
                val client = inject<IClientInterface>()
                client.setCredentials(loginData.username!!, loginData.password!!)
                loggedIn = true
                doGame()
                break
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
