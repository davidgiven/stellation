package model

import utils.Oid
import interfaces.throwNobodyLoggedInException
import interfaces.throwPermissionDeniedException
import kotlin.reflect.KClass

open class SPlayer(model: Model, oid: Oid) : SThing(model, oid) {
    var email by EMAIL_ADDRESS
    var password_hash by PASSWORD_HASH
    val frames by FRAMES
    val ships by SHIPS
}

fun SPlayer.isGod(): Boolean = (oid == GOD_OID)

fun SPlayer.checkGod() {
    if (!isGod()) {
        throwPermissionDeniedException()
    }
}

fun SPlayer.canSee(obj: SThing): Boolean {
    val objStar = obj.getContainingStar() ?: return false
    for (ship in ships) {
        if (ship.findChild<SJumpdrive>() != null) {
            val shipStar = ship.getContainingStar()
            if (shipStar == objStar) {
                return true
            }
        }
    }
    return false
}

fun SPlayer.calculateVisibleStars(): Set<SStar> {
    var set = HashSet<SStar>()
    for (ship in ships) {
        if (ship.findChild<SJumpdrive>() != null) {
            val star = ship.getContainingStar()
            if (star != null) {
                set.add(star)
            }
        }
    }
    return set
}

fun SPlayer.calculateVisibleObjects(): Set<SThing> {
    var set = HashSet<SThing>()
    set.add(this)
    for (star in calculateVisibleStars()) {
        set.addAll(star.calculateHierarchicalContents())
    }
    return set
}

fun Model.currentPlayer(): SPlayer {
    val oid = authenticator.currentPlayerOid
    if (oid == 0) {
        throwNobodyLoggedInException()
    }
    return loadObject(authenticator.currentPlayerOid, SPlayer::class)
}

fun Model.createNewPlayer(name: String, email: String): SPlayer {
    val player = createObject(SPlayer::class)
    player.name = name
    player.email = email
    authenticator.registerPlayer(email, player.oid)
    return player
}

fun Model.createPlayerFleet(player: SPlayer): SShip {
    val ship = createObject(SShip::class)
    ship.name = "${player.name}'s seedship"
    ship.owner = player
    player.ships += ship

    fun <T : SModule> addModule(moduleClass: KClass<T>) {
        val module = createObject(moduleClass)
        module.owner = player
        module.moveTo(ship)
    }

    addModule(SJumpdrive::class)
    addModule(STank::class)
    addModule(SAntimatterDistillery::class)
    addModule(SAsteroidMiner::class)
    addModule(SHydroponicsPlant::class)

    return ship
}