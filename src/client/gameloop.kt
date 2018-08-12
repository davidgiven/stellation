package client

import client.ui.Galaxy
import interfaces.IClientInterface
import interfaces.IClock
import interfaces.ICommandDispatcher
import interfaces.IConsole
import interfaces.IDatastore
import model.Model
import ui.AlertForm
import ui.LoginForm
import ui.SummaryWindow
import ui.show
import utils.Fault
import utils.FaultDomain.PERMISSION
import utils.Job
import utils.injection

class GameLoop {
    val cookies by injection<Cookies>()
    val clientInterface by injection<IClientInterface>()
    val commandDispatcher by injection<ICommandDispatcher>()
    val console by injection<IConsole>()
    val datastore by injection<IDatastore>()
    val clock by injection<IClock>()
    val model by injection<Model>()

    var galaxy: Galaxy? = null

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
                        clock.setTime(0.0)

                        val command = commandDispatcher.resolve(listOf("ping"))
                        command.run()

                        cookies["username"] = loginData.username
                        cookies["password"] = loginData.password
                        doGame()
                        break
                    } catch (f: Fault) {
                        if (f.domain == PERMISSION) {
                            AlertForm("Login failed", "Username or password unrecognised.").execute()
                        } else {
                            throw f
                        }
                    }
                }
            }
        }
    }

    private fun doGame() {
        galaxy = Galaxy(model.getGalaxy())
        galaxy!!.attach()

        val summary = SummaryWindow()
        summary.show()

        Job {
            console.println("Welcome to Stellation VI.")
            console.println("Try 'help' if you're feeling lucky.")
        }
    }
}
