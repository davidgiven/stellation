package model

import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.Oid
import interfaces.withSqlTransaction
import runtime.jvm.JvmDatabase
import runtime.shared.SqlDatastore
import utils.bind
import utils.inject
import utils.resetBindingsForTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TimersTest {
    private val database get() = inject<IDatabase>()
    private val datastore get() = inject<IDatastore>()
    private val model get() = inject<Model>()

    private lateinit var timers: Timers
    private lateinit var s1: SStar
    private lateinit var s2: SStar
    private lateinit var s3: SStar
    private lateinit var s4: SStar

    @BeforeTest
    fun setup() {
        resetBindingsForTest()
        bind<IDatabase>(JvmDatabase())
        bind<IDatastore>(SqlDatastore(database))
        bind(Model())

        database.openDatabase(":memory:")
        datastore.initialiseDatabase()
        database.withSqlTransaction { model.initialiseProperties() }
        database.executeSql("BEGIN")

        s1 = model.createObject(SStar::class)
        s2 = model.createObject(SStar::class)
        s3 = model.createObject(SStar::class)
        s4 = model.createObject(SStar::class)
        timers = Timers()
    }

    @AfterTest
    fun teardown() {
        database.executeSql("COMMIT")
        database.closeDatabase()
    }

    private fun getTimerCount(): Int =
            database.sqlStatement("SELECT COUNT(*) AS count FROM timers")
                    .executeSimpleQuery()!!
                    .get("count")!!
                    .getInt()

    @Test
    fun timerOrderTest() {
        timers.setTimer(s1.oid, 1)
        timers.setTimer(s2.oid, 10)
        timers.setTimer(s3.oid, 8)
        timers.setTimer(s4.oid, 99)

        var results = emptyList<Pair<Oid, Long>>()
        timers.processTimers(20) { oid, expiry ->
            results += Pair(oid, expiry)
        }

        assertEquals(
                listOf(
                        Pair(s1.oid, 1L),
                        Pair(s3.oid, 8L),
                        Pair(s2.oid, 10L)), results)
        assertEquals(1, getTimerCount())
    }

    @Test
    fun runOutOfTimersTest() {
        timers.setTimer(s1.oid, 1)
        timers.setTimer(s2.oid, 10)
        timers.setTimer(s3.oid, 8)

        timers.processTimers(20) { _, _ -> }

        assertEquals(0, getTimerCount())
    }
}
