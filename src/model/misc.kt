package model

import interfaces.Oid

open class SStar(model: Model, oid: Oid) : SThing(model, oid) {
    var name by NAME
    var x by XPOS
    var y by YPOS
    var brightness by BRIGHTNESS
    var asteroidsM by ASTEROIDS_M
    var asteroidsO by ASTEROIDS_O
}

open class SShip(model: Model, oid: Oid) : SThing(model, oid) {
    var name by NAME
}

open class SModule(model: Model, oid: Oid) : SThing(model, oid) {
    open val width = 1
    open val height = 1
}

open class SJumpdrive(model: Model, oid: Oid) : SModule(model, oid) {
    override val width = 3
    override val height = 3
}

open class STank(model: Model, oid: Oid) : SModule(model, oid) {
}

open class SCargo(model: Model, oid: Oid) : SModule(model, oid) {
}

open class SRefinery(model: Model, oid: Oid) : SModule(model, oid) {
}

open class SAntimatterDistillery(model: Model, oid: Oid) : SRefinery(model, oid) {
}

open class SAsteroidMiner(model: Model, oid: Oid) : SRefinery(model, oid) {
}

open class SHydroponicsPlant(model: Model, oid: Oid) : SRefinery(model, oid) {
}

open class SFactory(model: Model, oid: Oid) : SModule(model, oid) {
}

open class SWeapon(model: Model, oid: Oid) : SModule(model, oid) {
}

open class SRAMCannon(model: Model, oid: Oid) : SWeapon(model, oid) {
}
