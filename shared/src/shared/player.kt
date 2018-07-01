package shared

import datastore.Aggregate
import datastore.Oid

open class SPlayer(oid: Oid) : SThing(oid) {
    val name by primitive(NAME)
    val frames: Aggregate<SFrame>
        get() = aggregate(FRAMES)
}

