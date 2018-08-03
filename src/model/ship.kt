package model

import interfaces.Oid

open class SShip(model: Model, oid: Oid) : SThing(model, oid) {
    var name by NAME
}