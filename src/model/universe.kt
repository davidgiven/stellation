package model

import datastore.Oid
import utils.NameGenerator
import utils.random
import utils.roundBy
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

open class SUniverse(oid: Oid) : SThing(oid) {
    var galaxy by primitive(GALAXY)
    val players by aggregate(PLAYERS)
}

fun createNewUniverse(): SUniverse {
    val universe = createObject(SUniverse::class)
    check(universe.oid == 1)

    val nameGenerator = NameGenerator()
    var starNames = emptySet<String>()
    while (starNames.size < SGalaxy.NUMBER_OF_STARS) {
        starNames += nameGenerator.generate()
    }

    var starLocations = emptySet<Pair<Double, Double>>()
    while (starLocations.size < SGalaxy.NUMBER_OF_STARS) {
        val theta = random(0.0..(PI * 2.0))
        val x = (sin(theta) * SGalaxy.RADIUS).roundBy(10.0)
        val y = (cos(theta) * SGalaxy.RADIUS).roundBy(10.0)
        starLocations += Pair(x, y)
    }

    val galaxy = createObject(SGalaxy::class)
    universe.galaxy = galaxy

    starNames.zip(starLocations).forEach { (name, location) ->
        val star = createObject(SStar::class)
        star.name = name
        star.x = location.first
        star.y = location.second
        star.moveTo(galaxy)
    }

    // For test purposes, create a new player.

    createNewPlayer("TestPlayer")

    return universe
}

