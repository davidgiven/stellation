package runtime.konan

import interfaces.IDatabase
import interfaces.ILogger
import interfaces.ITime
import utils.bind

fun initKonanRuntime() {
    bind<ILogger>(KonanLogger())
    bind<ITime>(KonanTime())
    bind<IDatabase>(KonanDatabase())
}
