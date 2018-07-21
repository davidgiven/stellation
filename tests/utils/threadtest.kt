package utils

import interfaces.ILogger
import runtime.jvm.JvmLogger
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ThreadTest {
    companion object {
        init {
            bind<ILogger>(JvmLogger())
        }
    }

    @BeforeTest
    fun setup() {
        assertEquals(false, hasJobs())
    }

    @Test
    fun emptyScheduler() {
        runRunnableJobs()
    }

    @Test
    fun singleJob() {
        var state = 0

        Job {
            assertEquals(0, state++)
        }
        runRunnableJobs()
        assertEquals(1, state)
    }

    @Test
    fun twoJobs() {
        var state = 0

        Job {
            assertEquals(0, state++)
        }
        Job {
            assertEquals(1, state++)
        }
        runRunnableJobs()
        assertEquals(2, state)
    }

    @Test
    fun nestedJobs() {
        var state = 0

        Job {
            Job {
                assertEquals(1, state++)
            }
            assertEquals(0, state++)
        }
        runRunnableJobs()
        assertEquals(2, state)
    }

    @Test
    fun simpleSuspension() {
        var state = 0

        Job {
            assertEquals(1, state++)
            suspend()
            assertEquals(2, state++)
        }
        assertEquals(0, state++)
        runRunnableJobs()
        assertEquals(3, state++)
    }

    @Test
    fun mailboxSuspension() {
        var state = 0
        val mailbox = Mailbox<Boolean>()

        Job {
            assertEquals(0, state++)
            val result = mailbox.wait()
            assertEquals(true, result)
            assertEquals(3, state++)
        }

        runRunnableJobs()
        assertEquals(1, state++)
        assertEquals(false, hasRunnableJobs())
        mailbox.post(true)
        assertEquals(2, state++)
        runRunnableJobs()
        assertEquals(4, state)
    }

    private fun runRunnableJobs() {
        while (hasRunnableJobs()) {
            schedule()
        }
    }
}
