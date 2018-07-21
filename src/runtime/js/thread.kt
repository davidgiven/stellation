package runtime.js

import utils.hasRunnableJobs
import utils.schedule
import kotlin.browser.window

private var kicked = false

fun javascriptScheduler() {
    kicked = false
    if (hasRunnableJobs()) {
        schedule()
        if (hasRunnableJobs()) {
            kickScheduler()
        }
    }
}

fun kickScheduler() {
    if (!kicked) {
        kicked = true
        window.setTimeout(::javascriptScheduler, 0)
    }
}
