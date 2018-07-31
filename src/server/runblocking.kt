package server

import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.coroutines.experimental.startCoroutine

private val completionContinuation = object : Continuation<Unit> {
    override val context = EmptyCoroutineContext

    override fun resume(value: Unit) {
    }

    override fun resumeWithException(exception: Throwable) {
        throw exception
    }
}

fun runBlocking(block: suspend () -> Unit) {
    block.startCoroutine(completionContinuation)
}