package datastore

import com.google.common.truth.Truth.assertThat
import model.SStar
import model.createObject
import org.junit.After
import org.junit.Before
import org.junit.Test

class TimersTest {
    lateinit var s1: SStar
    lateinit var s2: SStar
    lateinit var s3: SStar
    lateinit var s4: SStar

    @Before
    fun setup() {
        openDatabase(":memory:")
        initialiseDatabase()
        s1 = createObject(SStar::class)
        s2 = createObject(SStar::class)
        s3 = createObject(SStar::class)
        s4 = createObject(SStar::class)
    }

    @After
    fun teardown() {
        closeDatabase()
    }

    private fun getTimerCount(): Int =
            sqlStatement("SELECT COUNT(*) AS count FROM timers")
                    .executeSimpleQuery()!!
                    .get("count")!!
                    .getInt()

    @Test
    fun timerOrderTest() {
        setTimer(s1.oid, 1)
        setTimer(s2.oid, 10)
        setTimer(s3.oid, 8)
        setTimer(s4.oid, 99)

        var results = emptyList<Pair<Oid, Long>>()
        processTimer(20) { oid, expiry ->
            results += Pair(oid, expiry)
        }

        assertThat(results).containsExactly(
                Pair(s1.oid, 1L),
                Pair(s3.oid, 8L),
                Pair(s2.oid, 10L)
        )

        assertThat(getTimerCount()).isEqualTo(1)
    }

    @Test
    fun runOutOfTimersTest() {
        setTimer(s1.oid, 1)
        setTimer(s2.oid, 10)
        setTimer(s3.oid, 8)

        processTimer(20) { _, _ -> }

        assertThat(getTimerCount()).isEqualTo(0)
    }
}
