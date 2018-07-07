package runtime.jvm

import interfaces.IContext

fun initJvmRuntime() {
    IContext.context = object : IContext() {
        override val logger = JvmLogger()
        override val time = JvmTime()
    }
}
