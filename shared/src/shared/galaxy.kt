package shared

import datastore.Aggregate
import datastore.Oid

open class SGalaxy(oid: Oid) : SThing(oid) {
    val stars: Aggregate<SStar>
        get() = aggregate(STARS)

    fun initialiseGalaxy() {
        for (i in 0..99) {
            val star = createObject(SStar::class)
            star.name = create_star_name()
            stars.add(star)
        }
    }
}

