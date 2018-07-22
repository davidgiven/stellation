package runtime.konan

import interfaces.ILogger
import interfaces.IEnvironment
import utils.get

class KonanLogger(val environment: IEnvironment = get()) : ILogger {
    override fun println(message: String) {
        environment.writeStderr("${message}\n")
    }
}
