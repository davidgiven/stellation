package runtime.jvm

import interfaces.IDatabase
import interfaces.IEnvironment
import interfaces.ILogger
import interfaces.ITime
import utils.bind

fun initJvmRuntime() {
    bind<ILogger>(JvmLogger())
    bind<ITime>(JvmTime())
    bind<IDatabase>(JvmDatabase())
    bind<IEnvironment>(JvmEnvironment())
}
