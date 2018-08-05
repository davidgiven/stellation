package model

import interfaces.IClock
import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.ISyncer
import interfaces.withSqlTransaction
import org.junit.Before
import org.junit.Test
import runtime.jvm.JvmDatabase
import runtime.shared.Clock
import runtime.shared.SqlDatastore
import runtime.shared.Syncer
import utils.bind
import utils.inject
import utils.resetBindingsForTest
import kotlin.test.assertEquals

class SyncTest {
    private val database get() = inject<IDatabase>()
    private val datastore get() = inject<IDatastore>()
    private val model get() = inject<Model>()
    private val clock get() = inject<IClock>()
    private val syncer get() = inject<ISyncer>()

    private lateinit var universe: SUniverse
    private lateinit var god: SPlayer
    private lateinit var star1: SStar
    private lateinit var star2: SStar
    private lateinit var player1: SPlayer
    private lateinit var player2: SPlayer
    private lateinit var ship1: SShip
    private lateinit var jumpdrive1: SJumpdrive
    private lateinit var ship2: SShip
    private lateinit var jumpdrive2: SJumpdrive

    @Before
    fun setup() {
        resetBindingsForTest()
        bind<IClock>(Clock())
        bind<IDatabase>(JvmDatabase())
        bind<IDatastore>(SqlDatastore())
        bind<ISyncer>(Syncer())
        bind(Model())

        clock.setTime(1.0)

        database.openDatabase(":memory:")
        datastore.initialiseDatabase()
        database.withSqlTransaction { model.initialiseProperties() }
        database.executeSql("BEGIN")

        universe = model.createObject(SUniverse::class)
        god = model.createObject(SPlayer::class)
        universe.galaxy = model.createObject(SGalaxy::class).moveTo(universe)

        star1 = model.createObject(SStar::class).moveTo(universe.galaxy!!)
        star2 = model.createObject(SStar::class).moveTo(universe.galaxy!!)

        player1 = model.createObject(SPlayer::class)
        player2 = model.createObject(SPlayer::class)

        ship1 = model.createObject(SShip::class)
        ship1.owner = player1
        player1.ships += ship1
        ship1.moveTo(star1)

        jumpdrive1 = model.createObject(SJumpdrive::class)
        jumpdrive1.owner = player1
        jumpdrive1.moveTo(ship1)

        ship2 = model.createObject(SShip::class)
        ship2.owner = player2
        player2.ships += ship2
        ship2.moveTo(star2)

        jumpdrive2 = model.createObject(SJumpdrive::class)
        jumpdrive2.owner = player2
        jumpdrive2.moveTo(ship2)
    }

    @Test
    fun visibleObjectsTest() {
        assertEquals(
                setOf(star1, player1, ship1, jumpdrive1),
                player1.calculateVisibleObjects())
        assertEquals(
                setOf(star2, player2, ship2, jumpdrive2),
                player2.calculateVisibleObjects())
    }

    @Test
    fun initialSyncTest() {
        val p = syncer.exportSyncPacket(player1.oid, 0.0)
        assertEquals(
                setOf(
                        universe.oid,
                        universe.galaxy!!.oid,
                        star1.oid,
                        star2.oid,
                        player1.oid,
                        ship1.oid,
                        jumpdrive1.oid),
                p.getVisibleObjects())
    }

    @Test
    fun incrementalSyncTest() {
        clock.setTime(3.0)
        star1.name = "Fnord"
        val p = syncer.exportSyncPacket(player1.oid, 1.0)
        val changed = p.getChangedProperties()
        assertEquals(listOf(Triple(star1.oid, "name", "Fnord")), changed)
    }
}
