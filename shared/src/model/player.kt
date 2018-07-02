package model

import datastore.Aggregate
import datastore.Oid

open class SPlayer(oid: Oid) : SThing(oid) {
    var name by primitive(NAME)
    var password_hash by primitive(PASSWORD_HASH)
    val frames by aggregate(FRAMES)
    val visible_ships by aggregate(VISIBLE_SHIPS)
    val all_ships by aggregate(ALL_SHIPS)
}

fun createNewPlayer(name: String): SPlayer {
    val player = createObject(SPlayer::class)
    player.name = name
    player.password_hash = "<password hash here>"

    val ship = createObject(SShip::class)
    ship.name = "$name's seedship"
    ship.owner = player
    player.all_ships += ship

    return player
}
