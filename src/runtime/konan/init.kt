package runtime.konan

import interfaces.IContext
import interfaces.ILogger
import interfaces.ITime
import interfaces.context

fun initKonanRuntime() {
    context = object : IContext() {
        override val logger: ILogger? = KonanLogger()
        override val time: ITime? = KonanTime()
    }
}
