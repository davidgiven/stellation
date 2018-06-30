package shared

import datastore.Aggregate
import datastore.AggregateProperty
import datastore.Oid
import datastore.PrimitiveProperty
import datastore.Proxy

abstract class SThing {
    var oid: Oid = -1

    protected fun <T> primitive(property: PrimitiveProperty<T>): Proxy<T> = property.get(this)
    protected fun <T> aggregate(property: AggregateProperty<T>): Aggregate<T> = property.get(this)

    var kind by primitive(KIND)
}

fun <T: SThing> T.bind(newOid: Oid): T {
    check(oid == -1)
    oid = newOid
    return this
}

fun <T: SThing> T.create(): T {
    check(oid == -1)
    oid = datastore.createObject(this::class.simpleName!!)
    return this
}

open class SUniverse : SThing() {
    var galaxy by primitive(GALAXY)

    val players: Aggregate<SPlayer>
        get() = aggregate(PLAYERS)
}

open class SStar : SThing() {
    var name by primitive(NAME)
    var x by primitive(XPOS)
    var y by primitive(YPOS)
    var brightness by primitive(BRIGHTNESS)
    var asteroidsM by primitive(ASTEROIDS_M)
    var asteroidsO by primitive(ASTEROIDS_O)
}

open class SPlayer : SThing() {
    var name by primitive(NAME)
}


open class SShip : SThing() {
    var name by primitive(NAME)
}

open class SModule : SThing() {
}

open class SFlaw : SModule() {
}

open class STank : SModule() {
}

open class SCargo : SModule() {
}

open class SFactory : SModule() {
}

open class SWeapon : SModule() {
}

open class SRAMCannon : SWeapon() {
}
