package model

import interfaces.Oid

open class SStar(model: Model, oid: Oid) : SThing(model, oid) {
    var name by NAME
    var x by XPOS
    var y by YPOS
    var brightness by BRIGHTNESS
}