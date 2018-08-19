package client

import client.ui.Galaxy
import interfaces.IClientInterface
import interfaces.IClock
import interfaces.ICommandDispatcher
import interfaces.IConsole
import interfaces.IDatastore
import interfaces.IUi
import interfaces.fireTickGlobalEvent
import model.Model
import runtime.shared.CommandShell
import ui.AlertForm
import ui.ConsoleWindow
import ui.LoginForm
import ui.SummaryWindow
import ui.show
import utils.Fault
import utils.FaultDomain.PERMISSION
import utils.Job
import utils.injection
import kotlin.browser.document
import kotlin.browser.window

class GameLoop: IConsole {
    val cookies by injection<Cookies>()
    val clientInterface by injection<IClientInterface>()
    val commandDispatcher by injection<ICommandDispatcher>()
    val datastore by injection<IDatastore>()
    val clock by injection<IClock>()
    val model by injection<Model>()
    val commandShell by injection<CommandShell>()
    val ui by injection<IUi>()

    var galaxy: Galaxy? = null
    var consoleWindow: ConsoleWindow? = null
    var summaryWindow: SummaryWindow? = null

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

        consoleWindow = ConsoleWindow(::onCommand)

        summaryWindow = SummaryWindow()

        consoleWindow!!.geometryChangeListeners += {
            val (_, cy) = it.element.getPosition()
            val (sw, _) = summaryWindow!!.element.getSize()
            summaryWindow!!.element.setSize(sw, cy-10)
        }

        summaryWindow!!.show()
        consoleWindow!!.show()

        Job {
            println("Welcome to Stellation VI.")
            println("Try 'help' if you're feeling lucky.")
        }

        window.setTimeout({ onTick() }, 1000)
    }

    private fun onTick() {
        ui.fireTickGlobalEvent()
        window.setTimeout({ onTick() }, 1000)
    }

    private fun onCommand(command: String) {
        Job {
            commandShell.call(command)
        }
    }

    override suspend fun println(message: String) {
        consoleWindow?.print(message)
    }
}
