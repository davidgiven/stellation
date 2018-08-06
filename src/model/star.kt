package model

import utils.Oid

open class SStar(model: Model, oid: Oid) : SThing(model, oid) {
    var x by XPOS
    var y by YPOS
    var brightness by BRIGHTNESS
}