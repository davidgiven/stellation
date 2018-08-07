@file:Suppress("NOTHING_TO_INLINE")

package utils

import utils.FaultDomain.INTERNAL

fun throwInvalidCodecDataException(s: String, e: Throwable? = null): Nothing =
        throw Fault(e).withDomain(INTERNAL).withDetail("invalid encoded packet: $s")

const val COUNT = "_count"

private const val ESCAPED_PERCENT = "%p"
private const val ESCAPED_NEWLINE = "%n"
private const val ESCAPED_EQUALS = "%e"

private val ENCODE_REGEX = Regex("[%=\n]")

private val ENCODE_TRANSFORMED = { r: MatchResult ->
    when (r.value) {
        "%" -> ESCAPED_PERCENT
        "\n" -> ESCAPED_NEWLINE
        "=" -> ESCAPED_EQUALS
        else -> r.value
    }
}

private val DECODE_REGEX = Regex("%.")

private val DECODE_TRANSFORMED = { r: MatchResult ->
    when (r.value) {
        ESCAPED_PERCENT -> "%"
        ESCAPED_NEWLINE -> "\n"
        ESCAPED_EQUALS -> "="
        else -> throwInvalidCodecDataException("bad string escape ${r.value}")
    }
}

private fun encodeString(string: String): String = ENCODE_REGEX.replace(string, ENCODE_TRANSFORMED)
private fun decodeString(string: String): String = DECODE_REGEX.replace(string, DECODE_TRANSFORMED)

open class Message : Iterable<String> {
    var _map: MutableMap<String, String> = HashMap()
    var count = 0

    inline val size: Int get() = getIntOrDefault(COUNT, 0)

    constructor()

    constructor(serialised: String) {
        deserialise(serialised)
    }

    constructor(map: Map<String, String>) {
        _map.clear()
        _map.putAll(map)
        count = _map[COUNT]?.toInt() ?: 0
    }

    fun serialise(): String {
        val sb = StringBuilder()

        val map = toMap()
        val keys = map.keys.toList().sorted()
        for (k in keys) {
            val v = map[k]!!
            sb.append(encodeString(k))
            sb.append('=')
            sb.append(encodeString(v))
            sb.append('\n')
        }

        return sb.toString()
    }

    fun deserialise(input: String) {
        _map.clear()

        input.lineSequence().forEach { line ->
            if (!line.isEmpty()) {
                val tokens = line.split("=")
                if (tokens.size != 2) {
                    throwInvalidCodecDataException("couldn't parse line")
                }
                _map[decodeString(tokens[0])] = decodeString(tokens[1])
            }
        }

        count = _map[COUNT]?.toInt() ?: 0
    }

    fun toMap(): Map<String, String> {
        if (count == 0) {
            _map.remove(COUNT)
        } else {
            _map[COUNT] = count.toString()
        }
        return _map
    }

    fun toList(): List<String> {
        val list = ArrayList<String>()
        for (i in 0..(count - 1)) {
            val arg: String = get(i)
            list += arg
        }
        return list
    }

    override fun iterator(): Iterator<String> = toList().iterator()

    inline operator fun <K> contains(key: K): Boolean = _map.containsKey(key.toString())

    inline fun <K> getCharOrNull(key: K) = _map[key.toString()]?.get(0)
    inline fun <K> getIntOrNull(key: K) = _map[key.toString()]?.toInt()
    inline fun <K> getShortOrNull(key: K) = _map[key.toString()]?.toShort()
    inline fun <K> getBooleanOrNull(key: K) = _map[key.toString()]?.toBoolean()
    inline fun <K> getFloatOrNull(key: K) = _map[key.toString()]?.toFloat()
    inline fun <K> getDoubleOrNull(key: K) = _map[key.toString()]?.toDouble()
    inline fun <K> getStringOrNull(key: K) = _map[key.toString()]

    inline fun <K> getCharOrDefault(key: K, value: Char) = _map[key.toString()]?.get(0) ?: value
    inline fun <K> getIntOrDefault(key: K, value: Int) = _map[key.toString()]?.toInt() ?: value
    inline fun <K> getShortOrDefault(key: K, value: Short) = _map[key.toString()]?.toShort() ?: value
    inline fun <K> getBooleanOrDefault(key: K, value: Boolean) = _map[key.toString()]?.toBoolean() ?: value
    inline fun <K> getFloatOrDefault(key: K, value: Float) = _map[key.toString()]?.toFloat() ?: value
    inline fun <K> getDoubleOrDefault(key: K, value: Double) = _map[key.toString()]?.toDouble() ?: value
    inline fun <K> getStringOrDefault(key: K, value: String) = _map[key.toString()] ?: value

    inline fun <K> getChar(key: K) = getCharOrNull(key)!!
    inline fun <K> getInt(key: K) = getIntOrNull(key)!!
    inline fun <K> getShort(key: K) = getShortOrNull(key)!!
    inline fun <K> getBoolean(key: K) = getBooleanOrNull(key)!!
    inline fun <K> getFloat(key: K) = getFloatOrNull(key)!!
    inline fun <K> getDouble(key: K) = getDoubleOrNull(key)!!
    inline fun <K> getString(key: K) = getStringOrNull(key)!!

    inline fun <K> setChar(key: K, value: Char) = _map.put(key.toString(), value.toString())
    inline fun <K> setInt(key: K, value: Int) = _map.put(key.toString(), value.toString())
    inline fun <K> setShort(key: K, value: Short) = _map.put(key.toString(), value.toString())
    inline fun <K> setBoolean(key: K, value: Boolean) = _map.put(key.toString(), value.toString())
    inline fun <K> setFloat(key: K, value: Float) = _map.put(key.toString(), value.toString())
    inline fun <K> setDouble(key: K, value: Double) = _map.put(key.toString(), value.toString())
    inline fun <K> setString(key: K, value: String) = _map.put(key.toString(), value)

    inline fun addChar(value: Char) = setChar(count++, value)
    inline fun addInt(value: Int) = setInt(count++, value)
    inline fun addShort(value: Short) = setShort(count++, value)
    inline fun addBoolean(value: Boolean) = setBoolean(count++, value)
    inline fun addFloat(value: Float) = setFloat(count++, value)
    inline fun addDouble(value: Double) = setDouble(count++, value)
    inline fun addString(value: String) = setString(count++, value)

    inline operator fun <K> set(key: K, value: String) = setString(key, value)
    inline operator fun <K> get(key: K): String = getString(key)

    inline fun <K> clear(key: K) {
        _map.remove(key.toString())
    }
}

