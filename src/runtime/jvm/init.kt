package runtime.jvm

import interfaces.IDatabase
import interfaces.IEnvironment
import interfaces.ILogger
import interfaces.ITime
import interfaces.IUtf8
import utils.bind

fun initJvmRuntime() {
    bind<IUtf8>(JvmUtf8())
    bind<ILogger>(JvmLogger())
    bind<ITime>(JvmTime())
    bind<IDatabase>(JvmDatabase())
    bind<IEnvironment>(JvmEnvironment())
}
