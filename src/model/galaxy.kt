package model

import interfaces.Oid

open class SGalaxy(model: Model, oid: Oid) : SThing(model, oid) {
    companion object {
        val RADIUS = 30
        val NUMBER_OF_STARS = 200
    }
}

