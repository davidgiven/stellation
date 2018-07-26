package runtime.konan

import interfaces.ILogger
import interfaces.IEnvironment
import utils.injection

class KonanLogger : ILogger {
    val environment by injection<IEnvironment>()
    override fun println(message: String) {
        environment.writeStderr("${message}\n")
    }
}
