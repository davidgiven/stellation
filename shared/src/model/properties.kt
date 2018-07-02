package model

import datastore.Scope
import datastore.floatProperty
import datastore.intProperty
import datastore.refProperty
import datastore.setProperty
import datastore.stringProperty

val ALL_SHIPS = setProperty(Scope.GLOBAL, "all_ships", SShip::class)
val ASTEROIDS_M = intProperty(Scope.LOCAL, "asteroids_m")
val ASTEROIDS_O = intProperty(Scope.LOCAL, "asteroids_o")
val BRIGHTNESS = floatProperty(Scope.GLOBAL, "brightness")
val DATA = stringProperty(Scope.LOCAL, "data")
val FRAMES = setProperty(Scope.LOCAL, "frames", SFrame::class)
val GALAXY = refProperty(Scope.GLOBAL, "galaxy", SGalaxy::class)
val HEIGHT = intProperty(Scope.LOCAL, "height")
val KIND = stringProperty(Scope.GLOBAL, "kind")
val LOCATION = refProperty(Scope.LOCAL, "location", SThing::class)
val NAME = stringProperty(Scope.LOCAL, "name")
val OWNER = refProperty(Scope.LOCAL, "owner", SPlayer::class)
val PLAYERS = setProperty(Scope.SERVERONLY, "players", SPlayer::class)
val STARS = setProperty(Scope.GLOBAL, "stars", SStar::class)
val VISIBLE_SHIPS = setProperty(Scope.GLOBAL, "visible_ships", SShip::class)
val WIDTH = intProperty(Scope.LOCAL, "width")
val XPOS = floatProperty(Scope.LOCAL, "x")
val YPOS = floatProperty(Scope.LOCAL, "y")
var PASSWORD_HASH = stringProperty(Scope.SERVERONLY, "password_hash")

fun initProperties() {
    // This function exists solely to force the variables above to be created at the right time.
}
