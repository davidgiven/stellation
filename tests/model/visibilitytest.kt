package model

import interfaces.IClock
import interfaces.IDatabase
import interfaces.IDatastore
import interfaces.withSqlTransaction
import org.junit.Before
import org.junit.Test
import runtime.jvm.JvmDatabase
import runtime.shared.Clock
import runtime.shared.SqlDatastore
import utils.bind
import utils.inject
import utils.resetBindingsForTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VisibilityTest {
    private val database get() = inject<IDatabase>()
    private val datastore get() = inject<IDatastore>()
    private val model get() = inject<Model>()

    private lateinit var universe: SUniverse
    private lateinit var star1: SStar
    private lateinit var star2: SStar
    private lateinit var player1: SPlayer
    private lateinit var player2: SPlayer
    private lateinit var player3: SPlayer
    private lateinit var p1ship1: SShip
    private lateinit var p1ship2: SShip
    private lateinit var p2ship1: SShip

    @Before
    fun setup() {
        resetBindingsForTest()
        bind<IClock>(Clock())
        bind<IDatabase>(JvmDatabase())
        bind<IDatastore>(SqlDatastore())
        bind(Model())

        database.openDatabase(":memory:")
        datastore.initialiseDatabase()
        database.withSqlTransaction { model.initialiseProperties() }
        database.executeSql("BEGIN")

        universe = model.createObject(SUniverse::class)
        universe.galaxy = model.createObject(SGalaxy::class).moveTo(universe)

        star1 = model.createObject(SStar::class).moveTo(universe.galaxy!!)
        star2 = model.createObject(SStar::class).moveTo(universe.galaxy!!)

        player1 = model.createObject(SPlayer::class)
        player2 = model.createObject(SPlayer::class)
        player3 = model.createObject(SPlayer::class)

        p1ship1 = createJumpship(player1).moveTo(star1)
        p1ship2 = createShip(player1).moveTo(star2)
        p2ship1 = createJumpship(player2).moveTo(star2)
    }

    @Test
    fun objectVisibility() {
        assertTrue(player1.canSee(p1ship1))
        assertFalse(player1.canSee(p1ship2))

        assertFalse(player2.canSee(p1ship1))
        assertTrue(player2.canSee(p1ship2))
        assertTrue(player2.canSee(p2ship1))

        assertFalse(player3.canSee(p1ship1))
        assertFalse(player3.canSee(p1ship2))
        assertFalse(player3.canSee(p2ship1))

    }

    @Test fun calculateVisibleStars() {
        assertEquals(setOf(star1), player1.calculateVisibleStars())
        assertEquals(setOf(star2), player2.calculateVisibleStars())
        assertEquals(emptySet(), player3.calculateVisibleStars())
    }

    private fun createShip(owner: SPlayer): SShip {
        val ship = model.createObject(SShip::class)
        ship.owner = owner
        owner.ships += ship
        return ship
    }

    private fun createJumpship(owner: SPlayer): SShip {
        val ship = createShip(owner)
        ship.owner = owner

        val jumpdrive = model.createObject(SJumpdrive::class)
        jumpdrive.owner = owner
        jumpdrive.moveTo(ship)

        return ship
    }
}
