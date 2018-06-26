package shared

import kotlin.reflect.KProperty

enum class Scope {
    SERVERONLY,
    PRIVATE,
    LOCAL,
    GLOBAL,
}

interface Aggregate<T> {
    fun add(item: T)
    fun remove(item: T)
    fun contains(item: T): Boolean
    fun getAll(): Array<T>
    fun getOne(): T
    fun forEach(action: (T) -> Unit)
}

interface Proxy<T> {
    operator fun getValue(thing: SThing, property: KProperty<*>): T
    operator fun setValue(thing: SThing, property: KProperty<*>, value: T)
}

abstract class DatabaseType<T> {
    abstract fun sqlType(): String
}

val STRING_TYPE = object : DatabaseType<String>() {
    override fun sqlType() = "TEXT"
}

val INT_TYPE = object : DatabaseType<Int>() {
    override fun sqlType() = "INTEGER"
}

val FLOAT_TYPE = object : DatabaseType<Double>() {
    override fun sqlType() = "REAL"
}

class RefType<T> : DatabaseType<T>() {
    override fun sqlType() = "INTEGER REFERENCES eav_Class(oid) ON DELETE CASCADE"
}

class SetType<T> : DatabaseType<T>() {
    override fun sqlType() = "INTEGER REFERENCES eav_Class(oid) ON DELETE CASCADE"
}

var properties: Map<String, Property<*>> = mapOf()

class Property<T>(scope: Scope, name: String, databaseType: DatabaseType<T>) {
    init {
        properties = properties.plus(Pair(name, this))
    }
}


val ASTEROIDS_M = Property(Scope.LOCAL, "asteroids_m", INT_TYPE)
val ASTEROIDS_O = Property(Scope.LOCAL, "asteroids_o", INT_TYPE)
val BRIGHTNESS = Property(Scope.GLOBAL, "brightness", FLOAT_TYPE)
val GALAXY = Property(Scope.GLOBAL, "galaxy", RefType<SGalaxy>())
val KIND = Property(Scope.GLOBAL, "kind", STRING_TYPE)
val NAME = Property(Scope.LOCAL, "name", STRING_TYPE)
val PLAYERS = Property(Scope.SERVERONLY, "players", SetType<SPlayer>())
val STARS = Property(Scope.GLOBAL, "stars", SetType<SStar>())
val XPOS = Property(Scope.LOCAL, "x", FLOAT_TYPE)
val YPOS = Property(Scope.LOCAL, "y", FLOAT_TYPE)
