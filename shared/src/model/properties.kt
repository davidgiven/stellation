package model

import datastore.Scope
import datastore.floatProperty
import datastore.intProperty
import datastore.refProperty
import datastore.setProperty
import datastore.stringProperty

val ASTEROIDS_M = intProperty(Scope.LOCAL, "asteroids_m")
val ASTEROIDS_O = intProperty(Scope.LOCAL, "asteroids_o")
val BRIGHTNESS = floatProperty(Scope.GLOBAL, "brightness")
val CONTENTS = setProperty(Scope.LOCAL, "contents", SThing::class)
val DATA = stringProperty(Scope.LOCAL, "data")
val FRAMES = setProperty(Scope.LOCAL, "frames", SFrame::class)
val GALAXY = refProperty(Scope.GLOBAL, "galaxy", SGalaxy::class)
val KIND = stringProperty(Scope.GLOBAL, "kind")
val LOCATION = refProperty(Scope.LOCAL, "location", SThing::class)
val NAME = stringProperty(Scope.LOCAL, "name")
val OWNER = refProperty(Scope.LOCAL, "owner", SPlayer::class)
val PASSWORD_HASH = stringProperty(Scope.SERVERONLY, "password_hash")
val PLAYERS = setProperty(Scope.SERVERONLY, "players", SPlayer::class)
val SHIPS = setProperty(Scope.GLOBAL, "ships", SShip::class)
val XPOS = floatProperty(Scope.LOCAL, "x")
val YPOS = floatProperty(Scope.LOCAL, "y")

fun initProperties() {
    // This function exists solely to force the variables above to be created at the right time.
}
