package model

import utils.Oid

open class SGalaxy(model: Model, oid: Oid) : SThing(model, oid) {
    companion object {
        const val RADIUS = 30.0
        const val NUMBER_OF_STARS = 200
    }
}

