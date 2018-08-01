package model

import interfaces.NobodyLoggedInException
import interfaces.Oid
import interfaces.PermissionDeniedException
import kotlin.reflect.KClass

open class SPlayer(model: Model, oid: Oid) : SThing(model, oid) {
    var name by NAME
    var email by EMAIL_ADDRESS
    var password_hash by PASSWORD_HASH
    val frames by FRAMES
    val ships by SHIPS
}

fun SPlayer.isGod(): Boolean = (oid == GOD_OID)

fun SPlayer.checkGod() {
    if (!isGod()) {
        throw PermissionDeniedException()
    }
}

fun Model.currentPlayer(): SPlayer {
    val oid = authenticator.currentPlayerOid
    if (oid == 0) {
        throw NobodyLoggedInException()
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

fun Model.createPlayerFleet(player: SPlayer) {
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
}