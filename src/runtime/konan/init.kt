package runtime.konan

import interfaces.IDatabase
import interfaces.ILogger
import interfaces.ITime
import interfaces.IEnvironment
import interfaces.IUtf8
import utils.bind

fun initKonanRuntime() {
    bind<IUtf8>(KonanUtf8())
    bind<IEnvironment>(KonanEnvironment())
    bind<ILogger>(KonanLogger())
    bind<ITime>(KonanTime())
    bind<IDatabase>(KonanDatabase())
}
