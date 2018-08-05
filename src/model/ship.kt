package model

import utils.Oid

open class SShip(model: Model, oid: Oid) : SThing(model, oid) {
    var name by NAME
}