package client

import ui.LoginForm
import utils.Job
import utils.get
import kotlin.browser.document
import interfaces.IConsole

private var loggedIn = false

fun startGame() {
    doLogin()
}

private fun doLogin() {
    Job {
        while (true) {
            val loginData = LoginForm().execute()
            if (!loginData.canceled) {
                loggedIn = true
                doGame()
                break
            }
        }
    }
}

private fun doGame() {
    Job {
        val console: IConsole = get()
        console.println("Welcome to Stellation VI.")
        console.println("Try 'help' if you're feeling lucky.")
    }
}
