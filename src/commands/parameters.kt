package commands

import utils.InvalidCodecDataException

class Parameters {
    var map: Map<String, String> = emptyMap()

    inner class Proxy(val key: String) {
        fun to(value: String) {
            map += key to value
        }

        fun to(value: Int) {
            map += key to value.toString()
        }

        fun to(value: Boolean) {
            map += key to (if (value) "1" else "0")
        }

        fun asString() = map[key]
        fun asInt() = map[key]?.toInt()

        fun asBoolean(): Boolean {
            val s = map[key]
            if (s == "0") return false
            if (s == "1") return true
            throw InvalidCodecDataException("bad boolean")
        }
    }

    fun toMap() = map

    fun param(key: String) = Proxy(key)
    fun param(index: Int) = Proxy(index.toString())

    fun success() = Proxy("_success")
    fun setSuccess(value: Boolean) = success().to(value)
    fun getSuccess() = success().asBoolean()

    fun command() = Proxy("_command")
    fun setCommand(value: String) = command().to(value)
    fun getCommand() = command().asString()
}
