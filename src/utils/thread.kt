package utils

import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.coroutines.experimental.startCoroutine
import kotlin.coroutines.experimental.suspendCoroutine

/* This implements a single, single-tasking thread (using coroutines) for running the
 * client business logic. It runs a queue of Jobs, to completion, one at a time; Jobs
 * can be submitted at any time. Jobs can wait on a Mailbox, which can be posted at
 * any point, but only the current Job can be suspended.
 */

typealias JobCallback = suspend Job.() -> Unit
typealias JobContinuation = Continuation<Unit>

private val jobQueue = ArrayList<Job>()
private var currentJob: Job? = null

fun hasJobs() = (currentJob != null) || !jobQueue.isEmpty()
fun hasRunnableJobs() = currentJob?.runnable ?: hasJobs()

fun schedule() {
    if (currentJob != null) {
        currentJob!!.resume()
    } else {
        currentJob = jobQueue.firstOrNull()
        if (currentJob != null) {
            jobQueue.removeAt(0)
            currentJob!!.start()
        }
    }
}

private val completionContinuation = object : Continuation<Unit> {
    override val context = EmptyCoroutineContext

    override fun resume(value: Unit) {
        currentJob = null
    }

    override fun resumeWithException(exception: Throwable) {
        currentJob = null
        throw exception
    }
}

class Mailbox<T> {
    private var waiting = true
    private var value: T? = null

    suspend fun wait(): T {
        while (waiting) {
            currentJob!!.runnable = false
            currentJob!!.suspend()
        }
        return value!!
    }

    fun post(value: T) {
        check(waiting)
        waiting = false
        this.value = value
        currentJob?.runnable = true
    }
}

class Job(val callback: JobCallback) {
    var continuation: JobContinuation? = null
    var runnable = true

    init {
        jobQueue += this
    }

    fun start() {
        callback.startCoroutine(this, completionContinuation)
    }

    fun resume() {
        continuation!!.resume(Unit)
    }

    suspend fun suspend() {
        suspendCoroutine<Unit> {
            continuation = it
        }
    }
}

