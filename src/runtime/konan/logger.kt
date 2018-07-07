package runtime.konan

import interfaces.ILogger

class KonanLogger: ILogger {
    override fun println(message: String) {
        kotlin.io.println(message)
    }
}
