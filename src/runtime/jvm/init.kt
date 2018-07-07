package runtime.jvm

import datastore.IDatabase
import interfaces.IContext
import interfaces.ILogger
import interfaces.ITime
import interfaces.context

fun initJvmRuntime() {
    context = object : IContext() {
        override val logger: ILogger? = JvmLogger()
        override val time: ITime? = JvmTime()
        override val database: IDatabase? = JvmDatabase()
    }
}
