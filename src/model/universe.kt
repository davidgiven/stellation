package model

import interfaces.IClock
import interfaces.ITime
import utils.Oid
import utils.NameGenerator
import utils.Random
import utils.inject
import utils.injection
import utils.roundBy
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

open class SUniverse(model: Model, oid: Oid) : SThing(model, oid) {
    var galaxy by GALAXY
    val players by PLAYERS
}

fun Model.createNewUniverse(): SUniverse {
    val clock = inject<IClock>()
    val time = inject<ITime>()
    clock.setTime(time.realtime())

    val random by injection<Random>()

    val universe = createObject(SUniverse::class)
    check(universe.oid == 1)

    val god = createNewPlayer("God", "god")
    check(god.oid == 2)

    val nameGenerator = NameGenerator()
    var starNames = emptySet<String>()
    while (starNames.size < SGalaxy.NUMBER_OF_STARS) {
        starNames += nameGenerator.generate()
    }

    var starLocations = emptySet<Pair<Double, Double>>()
    while (starLocations.size < SGalaxy.NUMBER_OF_STARS) {
        val theta = random.random(0.0..(PI * 2.0))
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

    return universe
}

