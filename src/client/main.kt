package client

import model.Model
import org.w3c.dom.Element
import runtime.js.initJsRuntime
import runtime.js.kickScheduler
import utils.bind
import utils.Codec
import kotlin.browser.document
import interfaces.IConsole
import commands.CommandDispatcher
import interfaces.ICommandDispatcher
import interfaces.IServerInterface
import runtime.js.RemoteServerInterface
import runtime.shared.CommandShell

fun main(argv: Array<String>) {
    initJsRuntime()
    bind(Codec())
    bind(Model())
    val console = Console()
    bind<IConsole>(console)
    bind<ICommandDispatcher>(CommandDispatcher())
    bind<IServerInterface>(RemoteServerInterface())
    bind(CommandShell())

    document.body!!.removeChildren()
    console.show()

    startGame()
    kickScheduler()
}

fun Element.removeChildren(): Element {
    while (lastChild != null) {
        removeChild(lastChild!!)
    }
    return this
}