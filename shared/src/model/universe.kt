package model

import datastore.Aggregate
import datastore.Oid

open class SUniverse(oid: Oid) : SThing(oid) {
    var galaxy by primitive(GALAXY)

    val players: Aggregate<SPlayer>
        get() = aggregate(PLAYERS)

    fun initialiseUniverse() {
        galaxy = createObject(SGalaxy::class)
        galaxy!!.initialiseGalaxy()
    }
}

