package runtime

import utils.UNIMPLEMENTED

actual fun println(vararg ss: String) {
    kotlin.io.println(ss)
}

actual fun exit(status: Int) {
    UNIMPLEMENTED()
}

