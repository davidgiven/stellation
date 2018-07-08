package model

import interfaces.Oid

open class SStar(oid: Oid) : SThing(oid) {
    var name by primitive(NAME)
    var x by primitive(XPOS)
    var y by primitive(YPOS)
    var brightness by primitive(BRIGHTNESS)
    var asteroidsM by primitive(ASTEROIDS_M)
    var asteroidsO by primitive(ASTEROIDS_O)
}

open class SShip(oid: Oid) : SThing(oid) {
    var name by primitive(NAME)
}

open class SModule(oid: Oid) : SThing(oid) {
    open val width = 1
    open val height = 1
}

open class SJumpdrive(oid: Oid) : SModule(oid) {
    override val width = 3
    override val height = 3
}

open class STank(oid: Oid) : SModule(oid) {
}

open class SCargo(oid: Oid) : SModule(oid) {
}

open class SRefinery(oid: Oid) : SModule(oid) {
}

open class SAntimatterDistillery(oid: Oid) : SRefinery(oid) {
}

open class SAsteroidMiner(oid: Oid) : SRefinery(oid) {
}

open class SHydroponicsPlant(oid: Oid) : SRefinery(oid) {
}

open class SFactory(oid: Oid) : SModule(oid) {
}

open class SWeapon(oid: Oid) : SModule(oid) {
}

open class SRAMCannon(oid: Oid) : SWeapon(oid) {
}
