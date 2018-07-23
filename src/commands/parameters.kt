package commands

class Parameters {
    var map: Map<String, String> = emptyMap()

    inner class Getter(val key: String) {
        fun to(value: String) {
            map += key to value
        }

        fun to(value: Int) {
            map += key to value.toString()
        }
    }

    inner class Setter(val key: String) {
        fun asString() = map[key]
        fun asInt() = map[key]?.toInt()
    }

    fun toMap() = map

    fun set(key: String) = Setter(key)
    fun set(index: Int) = Setter(index.toString())

    fun get(key: String) = Getter(key)
    fun get(index: Int) = Getter(index.toString())
}
