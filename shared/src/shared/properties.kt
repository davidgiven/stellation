package shared

import datastore.Scope
import datastore.floatProperty
import datastore.intProperty
import datastore.refProperty
import datastore.setProperty
import datastore.stringProperty

val ASTEROIDS_M = intProperty(Scope.LOCAL, "asteroids_m")
val ASTEROIDS_O = intProperty(Scope.LOCAL, "asteroids_o")
val BRIGHTNESS = floatProperty(Scope.GLOBAL, "brightness")
val GALAXY = refProperty(Scope.GLOBAL, "galaxy", ::SGalaxy)
val KIND = stringProperty(Scope.GLOBAL, "kind")
val NAME = stringProperty(Scope.LOCAL, "name")
val PLAYERS = setProperty<SPlayer>(Scope.SERVERONLY, "players")
val STARS = setProperty<SStar>(Scope.GLOBAL, "stars")
val XPOS = floatProperty(Scope.LOCAL, "x")
val YPOS = floatProperty(Scope.LOCAL, "y")

fun initProperties() {
    // This function exists solely to force the variables above to be created at the right time.
}
