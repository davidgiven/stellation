interface ObjectRef {
    fun <T> primitive(property: Property<T>): Proxy<T>
    fun <T> aggregate(property: Property<T>): Aggregate<T>
    fun <T> create(): T
}

open class SThing {
    var obj: ObjectRef? = null

    protected fun <T> primitive(property: Property<T>): Proxy<T> = obj!!.primitive(property)
    protected fun <T> aggregate(property: Property<T>): Aggregate<T> = obj!!.aggregate(property)
    protected fun <T> create(): T = obj!!.create()

    var kind by primitive(KIND)
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
