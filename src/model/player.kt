package model

import interfaces.Oid
import kotlin.reflect.KClass

open class SPlayer(model: Model, oid: Oid) : SThing(model, oid) {
    var name by NAME
    var password_hash by PASSWORD_HASH
    val frames by FRAMES
    val ships by SHIPS
}

fun Model.createNewPlayer(name: String, password_hash: String): SPlayer {
    val player = createObject(SPlayer::class)
    player.name = name
    player.password_hash = password_hash
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