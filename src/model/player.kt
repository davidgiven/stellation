package model

import datastore.Oid
import kotlin.reflect.KClass

open class SPlayer(oid: Oid) : SThing(oid) {
    var name by primitive(NAME)
    var password_hash by primitive(PASSWORD_HASH)
    val frames by aggregate(FRAMES)
    val ships by aggregate(SHIPS)
}

fun createNewPlayer(name: String): SPlayer {
    val player = createObject(SPlayer::class)
    player.name = name
    player.password_hash = "<password hash here>"

    val ship = createObject(SShip::class)
    ship.name = "$name's seedship"
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

    return player
}
