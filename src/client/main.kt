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
import org.w3c.dom.Element
import runtime.js.RemoteClientInterface
import runtime.js.initJsRuntime
import runtime.js.kickScheduler
import runtime.shared.Clock
import runtime.shared.CommandShell
import runtime.shared.Syncer
import server.LocalAuthenticator
import utils.Codec
import utils.Random
import utils.bind
import utils.inject
import kotlin.browser.document

fun main(argv: Array<String>) {
    initJsRuntime()
    val console = Console()
    bind<IConsole>(console)
    bind<ICommandDispatcher>(CommandDispatcher())
    bind<IClientInterface>(RemoteClientInterface())
    bind<IAuthenticator>(LocalAuthenticator())
    bind(CommandShell())
    bind(Codec())
    bind(Model())
    bind(Cookies())
    bind<IClock>(Clock())
    bind<ISyncer>(Syncer())
    val gameloop = bind(GameLoop())

    val time = inject<ITime>()
    bind(Random(time.nanotime()))

    document.body!!.removeChildren()
    console.show()

    gameloop.startGame()
    kickScheduler()
}

fun Element.removeChildren(): Element {
    while (lastChild != null) {
        removeChild(lastChild!!)
    }
    return this
}