package model

import interfaces.Oid

open class SGalaxy(oid: Oid) : SThing(oid) {
    companion object {
        val RADIUS = 30
        val NUMBER_OF_STARS = 200
    }
}

