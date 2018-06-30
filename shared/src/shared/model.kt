package shared

import datastore.Oid

open class SStar(oid: Oid) : SThing(oid) {
    var name by primitive(NAME)
    var x by primitive(XPOS)
    var y by primitive(YPOS)
    var brightness by primitive(BRIGHTNESS)
    var asteroidsM by primitive(ASTEROIDS_M)
    var asteroidsO by primitive(ASTEROIDS_O)
}

open class SPlayer(oid: Oid) : SThing(oid) {
    var name by primitive(NAME)
}


open class SShip(oid: Oid) : SThing(oid) {
    var name by primitive(NAME)
}

open class SModule(oid: Oid) : SThing(oid) {
}

open class SJumpdrive(oid: Oid) : SModule(oid) {
}

open class STank(oid: Oid) : SModule(oid) {
}

open class SCargo(oid: Oid) : SModule(oid) {
}

open class SFactory(oid: Oid) : SModule(oid) {
}

open class SWeapon(oid: Oid) : SModule(oid) {
}

open class SRAMCannon(oid: Oid) : SWeapon(oid) {
}
