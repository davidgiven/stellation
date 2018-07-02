package model

import datastore.Aggregate
import datastore.Oid

open class SPlayer(oid: Oid) : SThing(oid) {
    val name by primitive(NAME)
    val frames by aggregate(FRAMES)
}

