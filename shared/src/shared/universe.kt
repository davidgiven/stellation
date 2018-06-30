package shared

import datastore.Aggregate

open class SUniverse : SThing() {
        var galaxy by primitive(GALAXY)

        val players: Aggregate<SPlayer>
            get() = aggregate(PLAYERS)

    fun initialiseUniverse() {
        galaxy = SGalaxy().create()
        galaxy.initialiseGalaxy()
    }
}

