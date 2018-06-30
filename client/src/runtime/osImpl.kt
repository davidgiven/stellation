package runtime

import utils.UNIMPLEMENTED
import kotlin.js.Date

actual fun println(vararg ss: String) {
    kotlin.io.println(ss)
}

actual fun exit(status: Int) {
    UNIMPLEMENTED()
}

actual fun nanotime() = (Date().getTime() * 1e6).toLong()
