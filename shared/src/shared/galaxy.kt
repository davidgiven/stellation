package shared

import datastore.Aggregate

open class SGalaxy : SThing() {
    val stars: Aggregate<SStar>
        get() = aggregate(STARS)

    fun initialiseGalaxy() {
        for (i in 0..99) {
            val star = SStar().create()
            star.name = "Beetleguice"
            stars.add(star)
        }
    }
}

