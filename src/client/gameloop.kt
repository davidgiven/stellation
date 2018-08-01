package client

import interfaces.AuthenticationFailedException
import interfaces.IClientInterface
import interfaces.ICommandDispatcher
import interfaces.IConsole
import interfaces.IDatastore
import ui.AlertForm
import ui.LoginForm
import utils.Job
import utils.injection

class GameLoop {
    val cookies by injection<Cookies>()
    val clientInterface by injection<IClientInterface>()
    val commandDispatcher by injection<ICommandDispatcher>()
    val console by injection<IConsole>()
    val datastore by injection<IDatastore>()

    fun startGame() {
        doLogin()
    }

    private fun doLogin() {
        Job {
            while (true) {
                datastore.initialiseDatabase()

                val defaultUsername = cookies["username"] ?: ""
                val defaultPassword = cookies["password"] ?: ""

                val loginData = LoginForm(defaultUsername, defaultPassword).execute()
                if (!loginData.canceled) {
                    try {
                        clientInterface.setCredentials(loginData.username!!, loginData.password!!)

                        val command = commandDispatcher.resolve(listOf("ping"))
                        command.run()

                        cookies["username"] = loginData.username!!
                        cookies["password"] = loginData.password!!
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
            console.println("Welcome to Stellation VI.")
            console.println("Try 'help' if you're feeling lucky.")
        }
    }
}
