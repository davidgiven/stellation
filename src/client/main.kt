package client

import model.Model
import org.w3c.dom.Element
import runtime.js.initJsRuntime
import runtime.js.kickScheduler
import utils.bind
import utils.inject
import utils.Codec
import kotlin.browser.document
import interfaces.IConsole
import interfaces.ITime
import interfaces.nanotime
import commands.CommandDispatcher
import interfaces.IAuthenticator
import interfaces.ICommandDispatcher
import interfaces.IClientInterface
import interfaces.IClock
import runtime.js.RemoteClientInterface
import runtime.shared.Clock
import runtime.shared.CommandShell
import server.LocalAuthenticator
import utils.Random

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