package runtime.konan

import interfaces.IContext

fun initKonanRuntime() {
    IContext.context = object : IContext() {
//        override val logger = JvmLogger()
//        override val time = KonanTime()
    }
}
