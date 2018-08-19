package client

import commands.CommandDispatcher
import interfaces.IAuthenticator
import interfaces.IClientInterface
import interfaces.IClock
import interfaces.ICommandDispatcher
import interfaces.IConsole
import interfaces.ISyncer
import interfaces.ITime
import interfaces.nanotime
import model.Model
import runtime.js.RemoteClientInterface
import runtime.js.initJsRuntime
import runtime.js.kickScheduler
import runtime.shared.ClientClock
import runtime.shared.CommandShell
import runtime.shared.Syncer
import server.LocalAuthenticator
import utils.Random
import utils.bind
import utils.inject
import kotlin.browser.document

fun main(argv: Array<String>) {
    initJsRuntime()
    bind<ICommandDispatcher>(CommandDispatcher())
    bind<IClientInterface>(RemoteClientInterface())
    bind<IAuthenticator>(LocalAuthenticator())
    bind(CommandShell())
    bind(Model())
    bind(Cookies())
    bind<IClock>(ClientClock())
    bind<ISyncer>(Syncer())
    val gameloop = bind(GameLoop())
    bind<IConsole>(gameloop)

    val time = inject<ITime>()
    bind(Random(time.nanotime()))

    document.getElementById("loading")!!.remove()

    gameloop.startGame()
    kickScheduler()
}
