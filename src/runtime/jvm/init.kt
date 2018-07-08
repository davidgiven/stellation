package runtime.jvm

import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.ILogger
import interfaces.ITime
import runtime.shared.SqlDatastore
import utils.bind

fun initJvmRuntime() {
    bind<ILogger>(JvmLogger())
    bind<ITime>(JvmTime())
    val database = bind<IDatabase>(JvmDatabase())
    bind<IDatastore>(SqlDatastore(database))
}
