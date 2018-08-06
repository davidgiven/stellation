package model

import interfaces.IClock
import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.ISyncer
import interfaces.ITime
import interfaces.withSqlTransaction
import org.junit.Before
import org.junit.Test
import runtime.jvm.JvmDatabase
import runtime.jvm.JvmTime
import runtime.shared.Clock
import runtime.shared.SqlDatastore
import runtime.shared.Syncer
import utils.bind
import utils.inject
import utils.resetBindingsForTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
    private lateinit var ship1: SShip
    private lateinit var jumpdrive1: SJumpdrive
    private lateinit var ship2: SShip
    private lateinit var jumpdrive2: SJumpdrive

    private lateinit var playerx: SPlayer
    private lateinit var shipx: SShip
    private lateinit var jumpdrivex: SJumpdrive

    @Before
    fun setup() {
        resetBindingsForTest()
        bind<IClock>(Clock())
        bind<IDatabase>(JvmDatabase())
        bind<IDatastore>(SqlDatastore())
        bind<ISyncer>(Syncer())
        bind(Model())
        bind<ITime>(JvmTime())

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

        ship1 = model.createObject(SShip::class)
        ship1.owner = player1
        player1.ships += ship1
        ship1.moveTo(star1)

        jumpdrive1 = model.createObject(SJumpdrive::class)
        jumpdrive1.owner = player1
        jumpdrive1.moveTo(ship1)

        ship2 = model.createObject(SShip::class)
        ship2.owner = player1
        player1.ships += ship2
        ship2.moveTo(star2)

        jumpdrive2 = model.createObject(SJumpdrive::class)
        jumpdrive2.owner = player1
        jumpdrive2.moveTo(ship2)


        playerx = model.createObject(SPlayer::class)

        shipx = model.createObject(SShip::class)
        shipx.owner = playerx
        playerx.ships += shipx
        shipx.moveTo(star2)

        jumpdrivex = model.createObject(SJumpdrive::class)
        jumpdrivex.owner = playerx
        jumpdrivex.moveTo(shipx)
    }

    @Test
    fun syncSessionTest() {
        assertEquals(1, datastore.createSyncSession())
        assertEquals(2, datastore.createSyncSession())
    }

    @Test
    fun visibleObjectsTest() {
        assertEquals(
                setOf(star1, player1, ship1, jumpdrive1, star2, ship2, jumpdrive2, shipx, jumpdrivex),
                player1.calculateVisibleObjects())
        assertEquals(
                setOf(star2, playerx, shipx, jumpdrivex, ship2, jumpdrive2),
                playerx.calculateVisibleObjects())
    }

    @Test
    fun initialSyncTest() {
        val session = datastore.createSyncSession()

        val p = syncer.exportSyncPacket(player1.oid, session)
        assertEquals(
                setOf(
                        universe.oid,
                        universe.galaxy!!.oid,
                        star1.oid,
                        star2.oid,
                        player1.oid,
                        ship1.oid,
                        jumpdrive1.oid,
                        ship2.oid,
                        jumpdrive2.oid,
                        shipx.oid,
                        jumpdrivex.oid),
                p.getVisibleObjects())
    }

    @Test
    fun incrementalSyncTest() {
        val session = datastore.createSyncSession()

        /* Initial sync */

        syncer.exportSyncPacket(player1.oid, session)

        /* First incremental sync, with one changed property. */

        star1.name = "Fnord"
        var p = syncer.exportSyncPacket(player1.oid, session)
        assertEquals(listOf(Triple(star1.oid, "name", "Fnord")), p.getChangedProperties())

        /* Second incremental sync --- no changes. */

        p = syncer.exportSyncPacket(player1.oid, session)
        assertEquals(emptyList(), p.getChangedProperties())

        /* Third incremental sync. ship2 moves to star1, rendering star2 invisible. */

        ship2.moveTo(star1)
        p = syncer.exportSyncPacket(player1.oid, session)
        assertFalse(shipx.oid in p.getVisibleObjects())
        assertFalse(jumpdrivex.oid in p.getVisibleObjects())

        /* Fourth incremental sync; an invisible object changes state. */

        shipx.name = "Floop"
        p = syncer.exportSyncPacket(player1.oid, session)
        assertEquals(emptyList(), p.getChangedProperties())

        /* Fifth incremental sync; ship2 moves back to star2. Now player1 can see shipx's new name. */

        ship2.moveTo(star2)
        p = syncer.exportSyncPacket(player1.oid, session)
        assertTrue(shipx.oid in p.getVisibleObjects())
        assertTrue(jumpdrivex.oid in p.getVisibleObjects())
        assertTrue(p.getChangedProperties().contains(Triple(shipx.oid, "name", "Floop")))
    }
}
