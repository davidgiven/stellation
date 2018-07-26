@file:Suppress("NOTHING_TO_INLINE")

package utils

import kotlin.reflect.KClass

const val COUNT = "_count"
const val SUCCESS = "_success"
const val ERROR = "_error"

open class Message : Iterable<String> {
    var _map: Map<String, String> = emptyMap()
    var count = 0

    inline val size: Int get() = getOrDefault(COUNT, 0)

    fun setFromMap(map: Map<String, String>) {
        _map = map
        count = _map[COUNT]?.toInt() ?: 0
    }

    fun toMap(): Map<String, String> {
        if (count == 0) {
            _map -= COUNT
        } else {
            _map += COUNT to count.toString()
        }
        return _map
    }

    fun toList(): List<String> {
        var list: List<String> = emptyList()
        for (i in 0..(count - 1)) {
            val arg: String = get(i)
            list += arg
        }
        return list
    }

    override fun iterator(): Iterator<String> = toList().iterator()
}

inline fun <K> Message.clear(key: K) {
    _map -= key.toString()
}

inline operator fun <K, V> Message.set(key: K, value: V) {
    _map += key.toString() to value.toString()
}

@Suppress("UNCHECKED_CAST")
fun <K, V> Message.getOrNull(key: K, kclass: KClass<*>): V? {
    val value = _map[key.toString()]
    value ?: return null
    return when (kclass) {
        Char::class    -> value[0] as V
        Int::class     -> value.toInt() as V
        Short::class   -> value.toShort() as V
        Boolean::class -> value.toBoolean() as V
        Float::class   -> value.toFloat() as V
        Double::class  -> value.toDouble() as V
        String::class  -> value as V
        else           -> throw IllegalArgumentException()
    }
}

inline fun <K, reified V> Message.getOrNull(key: K): V? = getOrNull(key, V::class)

inline operator fun <K, reified V> Message.get(key: K): V = getOrNull(key)!!
inline fun <K, reified V> Message.getOrDefault(key: K, default: V): V = getOrNull(key) ?: default

inline operator fun <K> Message.contains(key: K): Boolean = key.toString() in _map

inline fun <reified V> Message.add(value: V): Message {
    set(count++, value)
    return this
}

